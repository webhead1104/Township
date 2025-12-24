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
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.model.Expansion;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
@Slf4j
public class ExpansionsScraper implements Scraper<Expansion> {
    public static final int MAX_EXPANSIONS = 357;
    private static final String BASE_URL = "https://township.fandom.com/wiki/Expansions";

    @Override
    public String id() {
        return "expansions";
    }

    @Override
    public List<Expansion> scrape() throws IOException {
        Document doc = fetchPage(BASE_URL);
        log.debug("Page fetched successfully");

        List<Expansion> expansions = new ArrayList<>();

        Elements tables = doc.select("table.article-table");
        log.debug("Found {} tables on the page", tables.size());

        int i = 1;
        int previousXp = 0;
        for (Element table : tables) {
            if (!table.text().toLowerCase().contains("town land expansion")) {
                log.warn("Found table that isn't expansion table?");
                continue;
            }
            log.debug("Parsing table {}", i);
            List<Expansion> tableExpansions = new ArrayList<>();
            Elements rows = table.select("tr");
            log.debug("Table {} has {} actual rows", i, rows.size() - 2);
            for (int j = 2; j < rows.size(); j++) {
                Element row = rows.get(j);
                Elements cells = row.select("td");
                if (cells.size() != 6) {
                    log.error("Found {} cells on the table {}, but expected 6 cells", cells.size(), i);
                    continue;
                }
                if (tableExpansions.size() + expansions.size() == 357) {
                    log.error("On the towncraft wiki expansions 357 and above do not have data!");
                    break;
                }
                Expansion expansion = new Expansion(cells, previousXp);
                tableExpansions.add(expansion);
                previousXp = expansion.getXp();
            }
            expansions.addAll(tableExpansions);
            i++;
        }

        if (expansions.size() != MAX_EXPANSIONS) {
            log.error("Expected exactly {} expansions, but got {}", MAX_EXPANSIONS, expansions.size());
        } else {
            log.debug("Got exactly {} expansions as expected from the wiki.", expansions.size());
        }
        return expansions;
    }

    @Override
    public Class<Expansion> resultType() {
        return Expansion.class;
    }
}
