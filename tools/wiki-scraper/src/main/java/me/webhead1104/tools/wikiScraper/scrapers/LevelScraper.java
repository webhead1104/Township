package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.model.Level;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
@Slf4j
public class LevelScraper implements Scraper<Level> {
    private static final String BASE_URL = "https://township.fandom.com/wiki/Level_up";

    @Override
    public String id() {
        return "levels";
    }

    @Override
    public List<Level> scrape() throws IOException {
        Document doc = fetchPage(BASE_URL);
        log.debug("Page fetched successfully");

        List<Level> levels = new ArrayList<>();

        Elements rows = doc.expectFirst("tbody").select("tr");
        log.debug("Table has {} actual rows", rows.size() - 1);

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cells = row.select("td");
            if (i == 1) {
                log.debug(cells.text());
            }
            if (cells.size() != 11) {
                log.error("Found {} cells, but expected 11 cells", cells.size());
                continue;
            }
            log.debug("Row {}", i);
            levels.add(new Level(cells, i));
            if (i == Main.MAX_LEVEL) {
                break;
            }
        }

        if (levels.size() != Main.MAX_LEVEL) {
            log.error("Expected exactly {} levels, but got {}", Main.MAX_LEVEL, levels.size());
        } else {
            log.debug("Got exactly {} levels as expected from the wiki.", levels.size());
        }
        return levels;
    }
}
