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
package me.webhead1104.tools.wikiScraper.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Scraper interface
 *
 * @param <T> the return type
 */
public interface Scraper<T> {
    String UA = "TowncraftWikiScraper/1.0.0 (https://github.com/webhead1104/towncraft; webhead1104@hotmail.com)";

    /**
     * The scraper ID
     *
     * @return the scraper ID
     */
    String id();

    /**
     * Run the scraper and return the results.
     *
     * @return the results
     */
    List<T> scrape() throws IOException;

    Class<T> resultType();

    default File save(List<T> data, File outDir) throws IOException {
        return Utils.saveJson(data, outDir, outputPath().getPath(), resultType());
    }

    default File outputPath() {
        return new File(id() + ".json");
    }

    default Document fetchPage(String url) throws IOException {
        return Jsoup.connect(url).userAgent(UA).get();
    }

    /**
     * Retrieve the results of another scraper that has already been executed
     * (either as a declared dependency or because it was run earlier).
     */
    default <E> List<E> getFrom(Class<? extends Scraper<?>> scraperClass, Class<E> elementType) {
        return ScraperRegistry.get(scraperClass, elementType);
    }
}
