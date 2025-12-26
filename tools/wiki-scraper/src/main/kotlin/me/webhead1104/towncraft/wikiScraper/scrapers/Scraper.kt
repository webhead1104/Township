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
package me.webhead1104.towncraft.wikiScraper.scrapers

import me.webhead1104.towncraft.wikiScraper.utils.Utils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

interface Scraper<T> {
    /**
     * The scraper ID
     * 
     * @return the scraper ID
     */
    fun id(): String

    /**
     * Run the scraper and return the results.
     * 
     * @return the results
     */
    fun scrape(): List<T>

    fun resultType(): Class<T>

    fun save(data: List<T>, outDir: File): File {
        return Utils.saveJson<T>(data, outDir, outputPath().path, resultType())
    }

    fun outputPath(): File {
        return File("${id()}.json")
    }

    fun fetchPage(url: String): Document {
        return Jsoup.connect(url).userAgent(UA).get()
    }

    /**
     * Retrieve the results of another scraper that has already been executed
     * (either as a declared dependency or because it was run earlier).
     */
    fun <E> getFrom(scraperClass: Class<out Scraper<*>>): MutableList<E> {
        return ScraperRegistry.get(scraperClass)
    }

    companion object {
        const val UA: String =
            "TowncraftWikiScraper/1.0.0 (https://github.com/webhead1104/towncraft; webhead1104@hotmail.com)"
    }
}
