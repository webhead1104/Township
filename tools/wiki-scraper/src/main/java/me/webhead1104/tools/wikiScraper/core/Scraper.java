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
