package me.webhead1104.tools.wikiScraper.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.core.Outputs;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
@Command(name = "wiki-scraper", mixinStandardHelpOptions = true, version = "wiki-scraper 1.0.0",
        description = "Scrapes the official township wiki for data.")
public class Main implements Callable<Integer> {
    private static final Map<String, Scraper<?>> SCRAPERS = new LinkedHashMap<>();

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
            SCRAPERS.put(key, scraper);
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

    private static <T> void runScraper(Scraper<T> scraper, File outDir) {
        try {
            long start = System.currentTimeMillis();
            log.info("Starting scraper: {}", scraper.id());

            List<T> data = scraper.scrape();

            File jsonFile = Outputs.saveJson(data, outDir, scraper.outputPath().getPath());
            log.info("Saved to: {}", jsonFile.getAbsolutePath());
            log.info("Scraper '{}' completed successfully in {}ms", scraper.id(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("Scraping failed for '{}': {}", scraper.id(), e.getMessage(), e);
            System.exit(1);
        }
    }

    @Override
    public Integer call() {
        if (debug) {
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.DEBUG);
            log.info("Debug enabled!");
        }

        if (list) {
            log.info("Discovered scrapers ({}):", SCRAPERS.size());
            SCRAPERS.forEach((id, scraper) -> log.info("- {} ({})", id, scraper.getClass().getSimpleName()));
            return 0;
        }

        if (runAll) {
            log.info("Running all scrapers...");
            for (Scraper<?> scraper : SCRAPERS.values()) {
                runScraper(scraper, outDir);
            }
            log.info("Finished running all scrapers.");
            return 0;
        }

        if (scraperId == null || scraperId.isEmpty()) {
            log.error("Error: You must specify a scraper with -s/--scraper, or use -l to list, or -a to run all.");
            return 1;
        }

        Scraper<?> scraper = SCRAPERS.get(scraperId);
        if (scraper == null) {
            log.error("Unknown scraper id: '{}'", scraperId);
            log.info("Use -l to list available scrapers.");
            return 1;
        }

        runScraper(scraper, outDir);
        return 0;
    }
}