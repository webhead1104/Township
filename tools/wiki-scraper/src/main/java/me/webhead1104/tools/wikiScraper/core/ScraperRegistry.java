package me.webhead1104.tools.wikiScraper.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Runtime registry for scraper results to allow inter-scraper dependencies.
 */
public final class ScraperRegistry {
    private static final Map<Class<? extends Scraper<?>>, List<?>> RESULTS = new HashMap<>();

    private ScraperRegistry() {
    }

    public static synchronized void clear() {
        RESULTS.clear();
    }

    public static synchronized void register(Class<? extends Scraper<?>> scraperClass, List<?> result) {
        RESULTS.put(scraperClass, result == null ? List.of() : List.copyOf(result));
    }

    @SuppressWarnings("unchecked")
    public static synchronized <E> List<E> get(Class<? extends Scraper<?>> scraperClass, Class<E> elementType) {
        List<?> list = RESULTS.get(scraperClass);
        if (list == null) return List.of();
        // Best-effort runtime type check
        for (Object o : list) {
            if (o != null && !elementType.isInstance(o)) {
                throw new IllegalStateException("ScraperRegistry type mismatch for " + scraperClass.getName()
                        + ": expected element type " + elementType.getName() + ", but got " + o.getClass().getName());
            }
        }
        return (List<E>) list;
    }
}
