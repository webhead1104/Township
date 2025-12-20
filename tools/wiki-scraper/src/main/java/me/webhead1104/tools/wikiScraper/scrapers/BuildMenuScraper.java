package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.Utils;
import me.webhead1104.tools.wikiScraper.model.BuildMenu;
import me.webhead1104.tools.wikiScraper.model.ConstructionMaterials;
import me.webhead1104.tools.wikiScraper.model.Price;
import me.webhead1104.tools.wikiScraper.model.tile.*;
import me.webhead1104.tools.wikiScraper.parser.Page;
import me.webhead1104.tools.wikiScraper.parser.Row;
import me.webhead1104.tools.wikiScraper.parser.Table;

import java.io.IOException;
import java.util.*;

@Slf4j
@Keep
public class BuildMenuScraper implements Scraper<BuildMenu> {
    @Override
    public String id() {
        return "buildMenus";
    }

    @Override
    public List<BuildMenu> scrape() throws IOException {
        Map<String, BuildMenu> buildMenus = new LinkedHashMap<>();

        for (BuildMenuTypes buildMenuType : BuildMenuTypes.values()) {
            log.debug("SCRAPING {} page = {}", buildMenuType, buildMenuType.getPage());
            Page page = new Page(fetchPage(buildMenuType.getPage()), buildMenuType.getCssSelector());
            Table table = page.getTables().getFirst();
            List<BuildMenu.Building> buildings = new ArrayList<>();
            for (int i = buildMenuType.getStartRow(); i < table.getRows().size(); i++) {
                if (i == buildMenuType.getEndRow()) {
                    break;
                }
                Row row = table.getRows().get(i);

                buildings.add(buildMenuType.parseRow(row));
            }
            buildings.removeIf(Objects::isNull);
            buildings.removeIf(building -> {
                log.debug("Remove if building key = {} level = {}", building.getKey(), building.getLevelNeeded());
                return building.getLevelNeeded() > Main.MAX_LEVEL;
            });
            if (buildMenuType == BuildMenuTypes.FARMING) {
                buildings.addAll(0, parsePlots());
            }
            buildings.removeIf(it -> "event_center".equals(it.getKey())); //todo Towncraft#108

            BuildMenu buildMenu = new BuildMenu(buildMenuType.name().toLowerCase(), buildings);
            buildMenus.put(buildMenuType.name().toLowerCase(), buildMenu);
        }
        for (BuildMenu value : buildMenus.values()) {
            log.debug(Utils.LOADER.buildAndSaveString(Utils.LOADER.build().createNode().setList(BuildMenu.Building.class, value.getActualBuildings())));
        }
        return new ArrayList<>(buildMenus.values());
    }

    @Override
    public Class<BuildMenu> resultType() {
        return BuildMenu.class;
    }

    private List<BuildMenu.Building> parsePlots() throws IOException {
        List<BuildMenu.Building> buildings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            buildings.add(new BuildMenu.Building("plot").withTile(new PlotTile()).withSize(new TileSize(1, 1)));
        }
        boolean isFirstTable = true;
        for (Table table : new Page(fetchPage("https://township.fandom.com/wiki/Fields"), ".article-table").getTables()) {
            table.getRowsBetween(isFirstTable ? 2 : 1, table.getRows().size()).forEach(row -> {
                BuildMenu.Building building = new BuildMenu.Building("plot");
                building.setPopulationNeeded(row.getValue(1).getAsPopulation());
                if (building.getPopulationNeeded() > Main.MAX_POPULATION) {
                    return;
                }
                building.setXp(row.getValue(2).getAsXp());
                building.setTile(new PlotTile());
                building.setSize(new TileSize(1, 1));
                buildings.add(building);
            });
            isFirstTable = false;
        }
        log.debug("plots = {}", Utils.LOADER.buildAndSaveString(Utils.LOADER.build().createNode().setList(BuildMenu.Building.class, buildings)));
        return buildings;
    }

    @Getter
    public enum BuildMenuTypes {
        HOUSING("Houses", ".article-table", 1, -1) {
            private String lastTime;
            private int lastXp;
            private TileSize lastTileSize;

            @Override
            BuildMenu.Building parseRow(Row row) {
                log.debug("Parsing row size = {} text = {}", row.getValues().size(), row.getText());

                if (row.getValues().size() > 5) {
                    String timeText = row.getValue(5).text();
                    if (timeText.matches(".*[DdHhMmSs].*")) {
                        lastTime = timeText.replaceAll("\\s*\\(except.*?\\)", "").replaceAll("n/a", "").trim();
                    }
                }

                if (row.getValues().size() > 6) {
                    String xpText = row.getValue(6).text();
                    if (xpText.matches(".*\\d.*xp.*")) {
                        String cleanXp = xpText.replaceAll("\\s*\\(.*?\\)", "").trim();
                        String xpNumber = cleanXp.replaceAll("xp.*", "").replaceAll("[^0-9]", "").trim();
                        if (!xpNumber.isEmpty()) {
                            lastXp = Integer.parseInt(xpNumber);
                        }
                    }
                }

                if (row.getValues().size() > 7 && row.getValue(7).text().matches(".*\\d.*")) {
                    lastTileSize = new TileSize(row.getValue(7).text());
                }

                BuildMenu.Building building = new BuildMenu.Building(row.getValue(1).getAsKey());
                building.setLevelNeeded(Integer.parseInt(row.getValue(2).text().split("@ lvl ")[1].split(" ")[0]));
                building.setLevelString(row.getValue(2).text());
                building.setPopulationIncrease(row.getValue(3).getAsPopulation());

                String priceText = row.getValue(4).text();
                String cleanPriceText = priceText.replaceAll("\\s*\\(free for.*?\\)", "").trim();
                String priceNumber = cleanPriceText.replaceAll("\\D+", "");
                building.setPrice(new Price(priceNumber.isEmpty() ? 0 : Integer.parseInt(priceNumber)));
                building.setPriceString(priceText);

                building.setTime(lastTime);
                building.setSize(lastTileSize);
                building.setTile(new HouseTile(row.getValue(1).getAsKey().toUpperCase()));
                building.setXp(lastXp);

                return building;
            }
        },
        COMMUNITY_BUILDINGS("Community_Buildings", ".sortable", 1, -1) {
            @Override
            BuildMenu.Building parseRow(Row row) {
                if (row.getValue(1).getAsLevel() >= 60) {
                    //todo community buildings above level 60 use drills, saws, and jackhammers
                    return null;
                }
                BuildMenu.Building building = new BuildMenu.Building(row.getValue(0).getAsKey());
                building.setLevelNeeded(row.getValue(1).getAsLevel());
                building.setMaxPopulationIncrease(row.getValue(2).getAsPopulation());
                building.setPrice(new Price(row.getValue(3).getAsCoins()));
                building.setConstructionMaterials(new ConstructionMaterials(row.getValue(4).text()));
                building.setTime(row.getValue(5).text());
                building.setSize(new TileSize(row.getValue(7).text()));
                building.setTile(new CommunityBuildingTile(row.getValue(0).getAsKey().toUpperCase()));
                building.setXp(row.getValue(6).getAsXp());
                return building;
            }
        }, FACTORIES("Factories", "table.article-table:nth-of-type(2)", 1, -1) {
            private String feedMillKey;
            private int feedMillCount = 2;

            @Override
            BuildMenu.Building parseRow(Row row) {
                log.debug("Parsing row size = {} text = {}", row.getValues().size(), row.getText());
                if (row.getValue(1).text().contains("x3")) {
                    feedMillKey = row.getValue(1).getAsKey();
                }
                if (row.getValues().size() == 6) {
                    BuildMenu.Building building = new BuildMenu.Building(feedMillKey);
                    building.setLevelNeeded(row.getValue(0).getAsLevel());
                    building.setPopulationNeeded(row.getValue(1).getAsPopulation());
                    building.setPrice(new Price(row.getValue(2).getAsCoins()));
                    building.setTime(row.getValue(3).text());
                    building.setXp(row.getValue(4).getAsXp());
                    building.setTile(new FactoryTile(feedMillKey.toUpperCase() + "_" + feedMillCount++));
                    return building;
                }
                BuildMenu.Building building = new BuildMenu.Building(row.getValue(1).getAsKey());
                building.setLevelNeeded(row.getValue(2).getAsLevel());
                building.setPopulationNeeded(row.getValue(3).getAsPopulation());
                building.setPrice(new Price(row.getValue(4).getAsCoins()));
                building.setTime(row.getValue(5).text());
                building.setSize(new TileSize(2, 2));
                String factoryType = row.getValue(1).getAsKey().equals("feed_mill") ? "feed_mill_1" : row.getValue(1).getAsKey();
                building.setTile(new FactoryTile(factoryType.toUpperCase()));
                building.setXp(row.getValue(6).getAsXp());
                return building;
            }
        },
        FARMING("Farming", ".wikitable", 1, 16) {
            private String lastName;
            private int count = 1;

            @Override
            BuildMenu.Building parseRow(Row row) {
                log.debug("Parsing row size = {} text = {}", row.getValues().size(), row.getText());
                if (row.getValues().size() == 9) {
                    lastName = row.getValue(0).text();
                    count = 1;
                }
                if (lastName.contains("Duck Feeder") || lastName.contains("Otter Pond") || lastName.contains("MushroomFarm")) {
                    //todo add support for duck feeder, otter pond, and mushroom farm
                    return null;
                }
                BuildMenu.Building building = new BuildMenu.Building(Utils.normalizeForKey(lastName));
                int offset = row.getValues().size() == 9 ? 0 : -1;
                building.setLevelNeeded(row.getValue(offset + 2).getAsLevel());
                log.debug("level = {}", row.getValue(offset + 2).text());
                building.setPrice(new Price(row.getValue(offset + 3).getAsCoins()));
                building.setTime(row.getValue(offset + 4).text().replaceAll("-", ""));
                building.setSize(new TileSize(2, 2));
                building.setTile(new AnimalTile(Utils.normalizeForKey(lastName) + "_" + count));
                building.setXp(row.getValue(offset + 5).getAsXp());
                count++;
                return building;
            }
        },
        SPECIAL("Special_Buildings", "table.article-table:nth-child(1)", 2, -1) {
            @Override
            BuildMenu.Building parseRow(Row row) {
                log.debug("Parsing row size = {} text = {}", row.getValues().size(), row.getText());
                if (row.getValues().size() != 7) {
                    return null;
                }
                BuildMenu.Building building = new BuildMenu.Building(row.getValue(0).getAsKey());
                if (row.getText().matches("[23]:")) {
                    log.debug("MATCHED TO LINE {}", row.getText());
                    return null;
                }
                if (row.getText().contains("Train")) {
                    building.setLevelNeeded(Integer.parseInt(row.getValue(1).text().replaceAll("Train \\d: ", "")));
                } else {
                    building.setLevelNeeded(row.getValue(1).getAsLevel());
                }
                log.debug("population = {}", row.getValue(2).text());
                building.setPopulationNeeded(row.getValue(2).getAsPopulation());
                building.setPrice(new Price(row.getValue(3).getAsCoins()));
                building.setTime(row.getValue(4).text().replaceAll("(n/a)|(Instant)", ""));
                building.setXp(row.getValue(5).getAsXp());
                building.setSize(new TileSize(row.getValue(6).text()));
                return building;
            }
        };
        private final String page;
        private final String cssSelector;
        private final int startRow;
        private final int endRow;

        BuildMenuTypes(String page, String cssSelector, int startRow, int endRow) {
            this.page = "https://township.fandom.com/wiki/" + page;
            this.cssSelector = cssSelector;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        BuildMenu.Building parseRow(Row row) {
            return null;
        }
    }
}
