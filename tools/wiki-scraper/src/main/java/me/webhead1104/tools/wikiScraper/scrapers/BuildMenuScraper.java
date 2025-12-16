package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.model.BuildMenu;
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
            List<Wrapper> buildings = new ArrayList<>();
            for (int i = buildMenuType.getStartRow(); i < table.getRows().size(); i++) {
                if (i == buildMenuType.getEndRow()) {
                    break;
                }
                Row row = table.getRows().get(i);

                buildings.add(buildMenuType.getRowParser().parse(row));
            }
            buildings.removeIf(Objects::isNull);
            buildings.removeIf(building -> building.level() > Main.MAX_LEVEL);
            buildMenus.put(buildMenuType.name().toLowerCase(), new BuildMenu(buildMenuType.name().toLowerCase(), new ArrayList<>(buildings.stream().map(Wrapper::key).toList())));
        }
        buildMenus.get("farming").getBuildings().addFirst("plot");
        buildMenus.get("special").getBuildings().remove("event_center"); //todo Towncraft#108
        return new ArrayList<>(buildMenus.values());
    }

    @Getter
    public enum BuildMenuTypes {
        HOUSING("Houses", ".article-table", 1, -1, row -> {
            int level = Integer.parseInt(row.getValue(2).text().split("@ lvl ")[1].split(" ")[0]);
            return new Wrapper(row.getValue(1).getAsKey(), level);
        }),
        COMMUNITY_BUILDINGS("Community_Buildings", ".sortable", 1, -1, row ->
                new Wrapper(row.getValue(0).getAsKey(), row.getValue(1).getAsLevel())),
        FACTORIES("Factories", "table.article-table:nth-of-type(2)", 1, -1, row -> {
            if (row.getValues().size() != 8) {
                return null;
            }
            return new Wrapper(row.getValue(1).getAsKey(), row.getValue(2).getAsLevel());
        }),
        FARMING("Farming", ".wikitable", 1, 16, row -> {
            if (row.getValues().size() != 9) {
                return null;
            }
            return new Wrapper(row.getValue(0).getAsKey().replaceAll("_💧", ""), row.getValue(2).getAsLevel());
        }),
        SPECIAL("Special_Buildings", "table.article-table:nth-child(1)", 2, -1, row -> {
            if (row.getValues().size() != 7) {
                return null;
            }

            if (row.getText().matches("[23]:")) {
                return null;
            }
            if (row.getText().contains("Train")) {
                return new Wrapper(row.getValue(0).getAsKey(), Integer.parseInt(row.getValue(1).text().replaceAll("Train \\d: ", "")));
            }

            return new Wrapper(row.getValue(0).getAsKey(), row.getValue(1).getAsLevel());
        });
        private final String page;
        private final String cssSelector;
        private final int startRow;
        private final int endRow;
        private final RowParser rowParser;

        BuildMenuTypes(String page, String cssSelector, int startRow, int endRow, RowParser rowParser) {
            this.page = "https://township.fandom.com/wiki/" + page;
            this.cssSelector = cssSelector;
            this.startRow = startRow;
            this.endRow = endRow;
            this.rowParser = rowParser;
        }
    }

    public interface RowParser {
        Wrapper parse(Row row);
    }

    public record Wrapper(String key, int level) {
    }
}
