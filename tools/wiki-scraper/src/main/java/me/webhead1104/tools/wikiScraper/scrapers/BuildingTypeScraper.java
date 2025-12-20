package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.annotations.DependsOn;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.Utils;
import me.webhead1104.tools.wikiScraper.model.BuildMenu;
import me.webhead1104.tools.wikiScraper.model.BuildingType;
import me.webhead1104.tools.wikiScraper.model.Price;
import me.webhead1104.tools.wikiScraper.model.tile.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Keep
@Slf4j
@DependsOn(BuildMenuScraper.class)
public class BuildingTypeScraper implements Scraper<BuildingType> {
    public static final Map<String, Tile> SPECIAL_BUILDING_TILES = new HashMap<>();

    static {
        SPECIAL_BUILDING_TILES.put("barn", new BarnTile());
        SPECIAL_BUILDING_TILES.put("helicopter", new HelicopterTile());
        SPECIAL_BUILDING_TILES.put("train", new TrainTile());
        SPECIAL_BUILDING_TILES.put("town_hall", StaticWorldTile.TOWN_HALL);
    }

    @Override
    public String id() {
        return "buildingTypes";
    }

    @Override
    public List<BuildingType> scrape() throws IOException {
        List<BuildingType> results = new ArrayList<>();
        for (BuildMenu buildMenu : getFrom(BuildMenuScraper.class, BuildMenu.class)) {
            List<BuildMenu.Building> buildings = new ArrayList<>();
            for (BuildMenu.Building actualBuilding : buildMenu.getActualBuildings()) {
                log.debug("PARSING {}", actualBuilding.getKey());

                if (buildMenu.getKey().equals("special")) {
                    actualBuilding.setTile(SPECIAL_BUILDING_TILES.get(actualBuilding.getKey()));
                    buildings.add(actualBuilding);
                } else if (buildMenu.getKey().equals("housing")) {
                    Pattern pattern = Pattern.compile("(\\d+) @ lvl (\\d+)");
                    Matcher matcher = pattern.matcher(actualBuilding.getLevelString());
                    int houseNumber = 1;
                    while (matcher.find()) {
                        int level = Integer.parseInt(matcher.group(2));
                        if (level > Main.MAX_LEVEL) {
                            houseNumber++;
                            continue;
                        }

                        boolean isFree = isFreeHouse(actualBuilding.getPriceString(), houseNumber);

                        Price price;
                        String time;
                        int xp;

                        if (isFree) {
                            price = new Price(0);
                            time = "";
                            xp = 0;
                        } else {
                            price = actualBuilding.getPrice();
                            time = actualBuilding.getTime();
                            if (time != null) {
                                time = time.replaceAll("\\(except .*\\)", "").trim();
                                time = time.replaceAll("[Nn]/[Aa]", "").trim();
                                time = time.replaceAll("Instant", "").trim();
                            }
                            xp = actualBuilding.getXp();
                        }

                        buildings.add(actualBuilding.withLevelNeeded(level).withPrice(price).withTime(time).withXp(xp));
                        houseNumber++;
                    }
                } else {
                    if (actualBuilding.getTime() != null) {
                        actualBuilding.setTime(actualBuilding.getTime().replaceAll("[Nn]/[Aa]", ""));
                        actualBuilding.setTime(actualBuilding.getTime().replaceAll("\\(except .*\\)", ""));
                        actualBuilding.setTime(actualBuilding.getTime().replaceAll("Instant", ""));
                    }
                    buildings.add(actualBuilding);
                }
            }
            results.add(new BuildingType(buildMenu.getKey(), buildings, buildMenu.getKey()));
        }

        for (BuildMenu.Building result : results.stream().flatMap(it -> it.getBuildings().stream()).toList()) {
            log.debug("RESULT {} level = {}", result.getKey(), result.getLevelNeeded());
        }
        return results;
    }

    private boolean isFreeHouse(String priceString, int houseNumber) {
        Pattern pattern = Pattern.compile("\\(free for #([\\d,]+)\\)");
        Matcher matcher = pattern.matcher(priceString);

        if (matcher.find()) {
            String numbersStr = matcher.group(1);
            String[] numbers = numbersStr.split(",");

            for (String num : numbers) {
                if (Integer.parseInt(num.trim()) == houseNumber) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public File save(List<BuildingType> data, File outDir) throws IOException {
        Map<String, List<BuildingType>> groupedByType = data.stream()
                .collect(Collectors.groupingBy(BuildingType::getType));


        for (Map.Entry<String, List<BuildingType>> entry : groupedByType.entrySet()) {
            String type = entry.getKey();
            List<BuildingType> typeGroup = entry.getValue();

            Utils.saveJson(typeGroup, new File(outDir, "buildingTypes"), type + ".json", BuildingType.class);
        }
        return new File(outDir, "buildingTypes");
    }

    @Override
    public Class<BuildingType> resultType() {
        return BuildingType.class;
    }
}
