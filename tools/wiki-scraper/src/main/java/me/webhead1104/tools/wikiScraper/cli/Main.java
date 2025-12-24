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
package me.webhead1104.tools.wikiScraper.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.annotations.DependsOn;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.ScraperRegistry;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
@Command(name = "wiki-scraper", mixinStandardHelpOptions = true, version = "wiki-scraper 1.0.0",
        description = "Scrapes the official towncraft wiki for data.")
public class Main implements Callable<Integer> {
    public static final int MAX_LEVEL = 10;
    public static final int MAX_POPULATION = 280;
    private static final Map<String, Scraper<?>> SCRAPERS_BY_ID = new LinkedHashMap<>();
    private static final Map<Class<? extends Scraper<?>>, Scraper<?>> SCRAPERS_BY_CLASS = new LinkedHashMap<>();

    static {
        List<Scraper<?>> scrapers = new ArrayList<>();
        try (ScanResult result = new ClassGraph().acceptPackages("me.webhead1104.tools.wikiScraper").enableAllInfo().scan()) {
            result.getClassesImplementing(Scraper.class).loadClasses().forEach(foundClass -> {
                try {
                    Constructor<?> constructor = foundClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Scraper<?> instance = (Scraper<?>) constructor.newInstance();
                    scrapers.add(instance);
                } catch (Exception e) {
                    log.error("An error occurred whilst getting scrapers!", e);
                }
            });
        } catch (Exception e) {
            log.error("An error occurred whilst getting scrapers!", e);
        }
        for (Scraper<?> scraper : scrapers) {
            String key = scraper.id();
            SCRAPERS_BY_ID.put(key, scraper);
            @SuppressWarnings("unchecked")
            Class<? extends Scraper<?>> clazz = (Class<? extends Scraper<?>>) scraper.getClass();
            SCRAPERS_BY_CLASS.put(clazz, scraper);
        }
    }

    @CommandLine.Option(names = {"-s", "--scraper"}, description = "The scraper id to run.")
    private String scraperId;
    @CommandLine.Option(names = {"-o", "--out"}, description = "The output directory.")
    private File outDir = new File("build/output");
    @CommandLine.Option(names = {"-l", "--list"}, description = "Lists all scraper ids.")
    private boolean list;
    @CommandLine.Option(names = {"-a", "--all"}, description = "Runs all scrapers.")
    private boolean runAll;
    @CommandLine.Option(names = {"-d", "--debug"}, description = "Enables debug mode.")
    private boolean debug;

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }

    private static <T> List<T> executeScraper(Scraper<T> scraper, File outDir) {
        try {
            long start = System.currentTimeMillis();
            log.info("Starting scraper: {}", scraper.id());

            List<T> data = scraper.scrape();

            File jsonFile = scraper.save(data, outDir);
            log.info("Saved to: {}", jsonFile.getAbsolutePath());
            log.info("Scraper '{}' completed successfully in {}ms", scraper.id(), System.currentTimeMillis() - start);
            return data;
        } catch (Exception e) {
            log.error("Scraping failed for '{}': {}", scraper.id(), e.getMessage(), e);
            System.exit(1);
            return List.of();
        }
    }

    private static void runWithDependencies(Scraper<?> scraper, File outDir,
                                            Set<Class<? extends Scraper<?>>> loaded,
                                            Set<Class<? extends Scraper<?>>> loading) {
        @SuppressWarnings("unchecked")
        Class<? extends Scraper<?>> clazz = (Class<? extends Scraper<?>>) scraper.getClass();
        if (loaded.contains(clazz)) return;
        if (loading.contains(clazz)) {
            throw new IllegalStateException("Circular dependency detected for: " + clazz.getName());
        }

        loading.add(clazz);

        DependsOn dependsOn = clazz.getAnnotation(DependsOn.class);
        if (dependsOn != null) {
            for (Class<? extends Scraper<?>> depClass : dependsOn.value()) {
                Scraper<?> dependency = SCRAPERS_BY_CLASS.get(depClass);
                if (dependency == null) {
                    // try to find assignable
                    dependency = SCRAPERS_BY_CLASS.entrySet().stream()
                            .filter(e -> depClass.isAssignableFrom(e.getKey()))
                            .map(Map.Entry::getValue)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException(
                                    "Dependency not found: " + depClass.getName() +
                                            " required by " + clazz.getName()));
                }
                runWithDependencies(dependency, outDir, loaded, loading);
            }
        }

        List<?> result = executeScraper(scraper, outDir);
        ScraperRegistry.register(clazz, result);

        loading.remove(clazz);
        loaded.add(clazz);
    }

    @Override
    public Integer call() {
        if (debug) {
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.DEBUG);
            log.info("Debug enabled!");
        }

        if (list) {
            log.info("Discovered scrapers ({}):", SCRAPERS_BY_ID.size());
            SCRAPERS_BY_ID.forEach((id, scraper) -> log.info("- {} ({})", id, scraper.getClass().getSimpleName()));
            return 0;
        }

        if (runAll) {
            log.info("Running all scrapers...");
            ScraperRegistry.clear();
            Set<Class<? extends Scraper<?>>> loaded = new HashSet<>();
            Set<Class<? extends Scraper<?>>> loading = new HashSet<>();
            for (Scraper<?> scraper : SCRAPERS_BY_ID.values()) {
                runWithDependencies(scraper, outDir, loaded, loading);
            }
            log.info("Finished running all scrapers.");
            return 0;
        }

        if (scraperId == null || scraperId.isEmpty()) {
            log.error("Error: You must specify a scraper with -s/--scraper, or use -l to list, or -a to run all.");
            return 1;
        }

        Scraper<?> scraper = SCRAPERS_BY_ID.get(scraperId);
        if (scraper == null) {
            log.error("Unknown scraper id: '{}'", scraperId);
            log.info("Use -l to list available scrapers.");
            return 1;
        }

        ScraperRegistry.clear();
        runWithDependencies(scraper, outDir, new HashSet<>(), new HashSet<>());
        return 0;
    }
}