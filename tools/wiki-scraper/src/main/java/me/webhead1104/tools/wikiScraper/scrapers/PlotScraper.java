package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.model.Plot;
import me.webhead1104.tools.wikiScraper.model.Price;
import me.webhead1104.tools.wikiScraper.parser.Page;
import me.webhead1104.tools.wikiScraper.parser.Row;
import me.webhead1104.tools.wikiScraper.parser.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
@Slf4j
public class PlotScraper implements Scraper<Plot> {
    private static final String BASE_URL = "https://township.fandom.com/wiki/Crops";

    @Override
    public String id() {
        return "plots";
    }

    @Override
    public List<Plot> scrape() throws IOException {
        List<Plot> plots = new ArrayList<>();
        plots.add(new Plot("none", new Price(0), 0, 0, "0s"));

        Table table = new Page(fetchPage(BASE_URL), ".fandom-table").getTables().getFirst();
        for (int i = 1; i < table.getRows().size(); i++) {
            Row row = table.getRow(i);
            plots.add(new Plot(
                    row.getValue(0).getAsKey(),
                    new Price(row.getValue(4).getAsCoins()),
                    row.getValue(9).getAsLevel(),
                    row.getValue(10).getAsXp(),
                    row.getValue(2).text()
            ));
        }

        plots.removeIf(plot -> plot.getLevelNeeded() > Main.MAX_LEVEL);
        return plots;
    }

    @Override
    public Class<Plot> resultType() {
        return Plot.class;
    }
}
