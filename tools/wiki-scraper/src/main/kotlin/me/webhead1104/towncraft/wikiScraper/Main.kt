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
package me.webhead1104.towncraft.wikiScraper

import ch.qos.logback.classic.Level
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.terminal.Terminal
import io.github.classgraph.ClassGraph
import me.webhead1104.towncraft.wikiScraper.annotations.DependsOn
import me.webhead1104.towncraft.wikiScraper.scrapers.Scraper
import me.webhead1104.towncraft.wikiScraper.scrapers.ScraperRegistry
import org.apache.commons.lang3.time.StopWatch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.reflect.Constructor

class Main : CliktCommand(name = "wiki-scraper") {
    val runAll: Boolean by option("-a", "--all", help = "Run all scrapers").flag()
    val scraperIds: List<String> by option("-s", "--scraper", help = "The scraper id").multiple(required = false)
    val listScrapers: Boolean by option("-l", "--list", help = "List scrapers").flag()
    val outDir: File by option("-o", "--out", help = "The output directory").file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    ).default(File("build/output"))
    val debug: Boolean by option("-d", "--debug", help = "Enable debug").flag()

    override fun run() {
        if (runAll && scraperIds.isNotEmpty()) {
            throw UsageError("Cannot use --all with specific scraper IDs")
        }

        if (debug) {
            val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger
            root.level = Level.DEBUG
            log.info("Debug enabled!")
        }

        when {
            listScrapers -> {
                log.info("Discovered scrapers ({}):", SCRAPERS_BY_ID.size)
                SCRAPERS_BY_ID.forEach { (id, scraper) ->
                    log.info(
                        "- {} ({})",
                        id,
                        scraper.javaClass.getSimpleName()
                    )
                }
            }

            runAll -> {
                log.info("Running all scrapers ${SCRAPERS_BY_ID.keys.joinToString(", ")}")
                ScraperRegistry.clear()
                val loaded: MutableSet<Class<out Scraper<*>>> = HashSet()
                val loading: MutableSet<Class<out Scraper<*>>> = HashSet()
                for (scraper in SCRAPERS_BY_ID.values) {
                    runWithDependencies(scraper, loaded, loading)
                }
                log.info("Finished running all scrapers.")
            }

            scraperIds.isNotEmpty() -> {
                log.info("Running scrapers: ${scraperIds.joinToString(", ")}")
                scraperIds.forEach { id ->
                    if (!SCRAPERS_BY_ID.containsKey(id)) {
                        throw UsageError("Scraper '$id' not found")
                    }
                    ScraperRegistry.clear()
                    val loaded: MutableSet<Class<out Scraper<*>>> = HashSet()
                    val loading: MutableSet<Class<out Scraper<*>>> = HashSet()

                    runWithDependencies(SCRAPERS_BY_ID[id]!!, loaded, loading)
                }
            }
        }
    }

    private fun runWithDependencies(
        scraper: Scraper<*>,
        loaded: MutableSet<Class<out Scraper<*>>>,
        loading: MutableSet<Class<out Scraper<*>>>
    ) {
        val clazz = scraper.javaClass
        if (loaded.contains(clazz)) return
        check(!loading.contains(clazz)) { "Circular dependency detected for: ${clazz.name}" }

        loading.add(clazz)

        val dependsOn = clazz.getAnnotation(DependsOn::class.java)
        if (dependsOn != null) {
            for (depClass in dependsOn.value) {
                var dependency = SCRAPERS_BY_CLASS[depClass.java]
                if (dependency == null) {
                    dependency = SCRAPERS_BY_CLASS.entries
                        .find { depClass.java.isAssignableFrom(it.key) }
                        ?.value
                        ?: throw IllegalStateException(
                            "Dependency not found: ${depClass.java.name} required by ${clazz.name}"
                        )
                }
                runWithDependencies(dependency, loaded, loading)
            }
        }

        val result = runScraper(scraper)
        ScraperRegistry.register(clazz, result.toMutableList())

        loading.remove(clazz)
        loaded.add(clazz)
    }

    fun <T> runScraper(scraper: Scraper<T>): List<T> {
        try {
            val stopWatch: StopWatch = StopWatch.createStarted()
            log.info("Starting scraper: {}", scraper.id())

            val data: List<T> = scraper.scrape()

            val jsonFile = scraper.save(data, outDir)
            log.info(
                "Scraper {} completed successfully in {}ms, saved to {}",
                scraper.id(), stopWatch.duration.toMillis(), jsonFile.absolutePath
            )
            return data
        } catch (e: Exception) {
            log.error("Scraping failed for {}", scraper.id(), e)
            return emptyList()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger("Wiki Scraper")
        private val SCRAPERS_BY_ID: MutableMap<String, Scraper<*>> = LinkedHashMap()
        private val SCRAPERS_BY_CLASS: MutableMap<Class<out Scraper<*>>, Scraper<*>> = LinkedHashMap()
        const val MAX_LEVEL: Int = 10
        const val MAX_POPULATION: Int = 280

        init {
            val scrapers: MutableSet<Scraper<*>> = HashSet()
            try {
                ClassGraph().acceptPackages("me.webhead1104.towncraft.wikiScraper")
                    .enableAllInfo().scan().use { result ->
                        result.getClassesImplementing(Scraper::class.java).loadClasses().forEach { foundClass ->
                            try {
                                val constructor: Constructor<*> = foundClass.getDeclaredConstructor()
                                constructor.isAccessible = true
                                val instance: Scraper<*> = constructor.newInstance() as Scraper<*>
                                scrapers.add(instance)
                            } catch (e: Exception) {
                                log.error("An error occurred whilst getting scrapers!", e)
                            }
                        }
                    }
            } catch (e: Exception) {
                log.error("An error occurred while loading scrapers", e)
            }

            for (scraper in scrapers) {
                log.info("Loading scraper {}", scraper.id())
                SCRAPERS_BY_ID[scraper.id()] = scraper
                SCRAPERS_BY_CLASS[scraper.javaClass as Class<out Scraper<*>>] = scraper
            }
        }
    }
}

fun main(args: Array<String>) = Main().context {
    terminal = Terminal(ansiLevel = AnsiLevel.TRUECOLOR, interactive = true)
}.main(args)