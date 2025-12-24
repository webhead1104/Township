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
