/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
