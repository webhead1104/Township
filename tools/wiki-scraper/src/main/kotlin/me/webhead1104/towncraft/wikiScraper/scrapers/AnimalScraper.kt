/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
import io.github.oshai.kotlinlogging.KotlinLogging
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.utils.Page
import me.webhead1104.towncraft.wikiScraper.utils.Row
import me.webhead1104.towncraft.wikiScraper.utils.Table
import me.webhead1104.towncraft.wikiScraper.utils.Utils
import org.jsoup.nodes.Element
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.*

@Keep
class AnimalScraper : Scraper<AnimalScraper.Animal> {
    val logger = KotlinLogging.logger {}
    override fun id(): String {
        return "animals"
    }

    override fun scrape(): MutableList<Animal> {
        val animals: MutableList<Animal> = ArrayList()

        val goodsTable = Page(fetchPage(BASE_URL), "div.mw-collapsible").tables[1]
        val urls: MutableList<String> = goodsTable.rows[2].element.child(0).child(0).children()
            .stream().map { element: Element -> element.absUrl("href") }.toList()
        var i = 0
        for (url in urls) {
            if (url.contains("Duck_Feeder") || url.contains("Otter_Pond") || url.contains("Mushroom_Farm")) {
                //todo add support for duck feeder, otter pond, and mushroom farm
                continue
            }
            logger.debug { url }
            val document = fetchPage(url)
            val page = Page(document, "table.wikitable:nth-child(8)")
            val table: Table = page.tables.first()
            val buildingTable = Table(page.document.expectFirst("table.wikitable:nth-child(4) > tbody:nth-child(1)"))

            val name = document.select(".mw-page-title-main").text()
            val key = url.split("https://township.fandom.com/wiki/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].lowercase(
                Locale.getDefault()
            )
            val adjustment = if (i > 0) 1 else 0
            val animal = Animal(
                key = key,
                amount = buildingTable.rows.stream().filter { row: Row? -> row!!.text.startsWith(name) }.toList().size,
                name = name,
                animalName = table.getRow(1).values.first().text.split(" Feed x3".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0],
                material = ANIMAL_DATA.materials.getOrDefault(
                    key.replace("_\\d+$".toRegex(), ""),
                    "minecraft:player_head"
                ),
                feed = table.getRow(1).values.first().key,
                product = table.getRow(2).values.first().key,
                xpClaim = table.getRow(2).values[4 + adjustment].xp,
                time = table.getRow(2).values[3 + adjustment].key,
                buildingKey = key.replace("_\\d+$".toRegex(), ""),
                levelNeeded = table.getRow(2).getValue(2).level
            )
            animals.add(animal)
            i++
        }
        animals.removeIf { animal: Animal -> animal.levelNeeded > Main.MAX_LEVEL }
        return animals
    }

    override fun resultType(): Class<Animal> {
        return Animal::class.java
    }

    @ConfigSerializable
    data class Animal(
        val key: String,
        val amount: Int,
        val name: String,
        @Setting("animal_name")
        val animalName: String,
        val material: String,
        val feed: String,
        val product: String,
        @Setting("xp_claim")
        val xpClaim: Int,
        val time: String,
        @Setting("building_key")
        val buildingKey: String,
        @Transient
        val levelNeeded: Int
    )

    @ConfigSerializable
    data class AnimalJson(val materials: Map<String, String> = emptyMap())

    companion object {
        val ANIMAL_DATA: AnimalJson = Utils.readJson(
            "/data/animal_data.json",
            AnimalJson::class.java
        )
        private const val BASE_URL = "https://township.fandom.com/wiki/Goods"
    }
}
