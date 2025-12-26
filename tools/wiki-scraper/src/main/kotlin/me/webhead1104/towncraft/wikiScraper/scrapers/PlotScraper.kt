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

import com.google.errorprone.annotations.Keep
import lombok.extern.slf4j.Slf4j
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.data.Price
import me.webhead1104.towncraft.wikiScraper.utils.Page
import me.webhead1104.towncraft.wikiScraper.utils.Table
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@Keep
@Slf4j
class PlotScraper : Scraper<PlotScraper.Plot> {
    override fun id(): String {
        return "plots"
    }

    override fun scrape(): MutableList<Plot> {
        val plots: MutableList<Plot> = ArrayList()
        plots.add(Plot("none", Price(), 0, 0, "0s"))

        val table: Table = Page(fetchPage(BASE_URL), ".fandom-table").tables.first()
        for (i in 1..<table.rows.size) {
            val row = table.getRow(i)
            plots.add(
                Plot(
                    row.getValue(0).key,
                    Price(amount = row.getValue(4).coins),
                    row.getValue(9).level,
                    row.getValue(10).xp,
                    row.getValue(2).text
                )
            )
        }

        plots.removeIf { plot -> plot.levelNeeded > Main.MAX_LEVEL }
        return plots
    }

    override fun resultType(): Class<Plot> {
        return Plot::class.java
    }

    @ConfigSerializable
    data class Plot(
        val key: String,
        val price: Price,
        @Setting("level_needed")
        val levelNeeded: Int,
        @Setting("xp_given")
        val xpGiven: Int,
        val time: String
    )

    companion object {
        private const val BASE_URL = "https://township.fandom.com/wiki/Crops"
    }
}
