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
import me.webhead1104.towncraft.wikiScraper.annotations.DependsOn
import me.webhead1104.towncraft.wikiScraper.data.Item
import me.webhead1104.towncraft.wikiScraper.utils.Utils
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting


@Keep
@DependsOn(ItemScraper::class)
class FactoryScraper : Scraper<FactoryScraper.Factory> {
    override fun id(): String {
        return "factories"
    }

    override fun scrape(): List<Factory> {
        val factories: MutableList<Factory> = ArrayList()
        val factoryItems: MutableMap<String, MutableList<Item>> = HashMap()
        getFrom<Item>(ItemScraper::class.java).stream()
            .filter { item -> item.factoryData != null }
            .forEach { item ->
                if (!factoryItems.containsKey(item.factoryData!!.factoryName)) {
                    factoryItems[item.factoryData!!.factoryName] = ArrayList()
                }
                factoryItems[item.factoryData!!.factoryName]!!.add(item)
            }

        factoryItems.forEach { (factoryName, items) ->
            val recipes: MutableList<Recipe> = ArrayList()
            for (item in items) {
                val factoryData: Item.FactoryData = item.factoryData!!
                recipes.add(
                    Recipe(
                        item.key,
                        if (factoryData.itemName.contains("x3")) 3 else 1,
                        factoryData.ingredients,
                        factoryData.time,
                        item.levelNeeded,
                        factoryData.xpGiven
                    )
                )
            }
            val factoryKey: String = Utils.normalizeForKey(factoryName)
            factories.add(
                Factory(
                    factoryKey,
                    if (factoryKey == "feed_mill") 3 else -1,
                    factoryName,
                    factoryKey,
                    recipes
                )
            )
        }
        factories.sortWith(Comparator.comparingInt { factory ->
            factory.recipes.stream()
                .mapToInt(Recipe::levelNeeded)
                .min()
                .orElse(Int.MAX_VALUE)
        })

        return factories
    }

    override fun resultType(): Class<Factory> {
        return Factory::class.java
    }


    @ConfigSerializable
    data class Factory(
        val key: String,
        val amount: Int,
        val name: String,
        @Setting("building_key")
        val buildingKey: String,
        val recipes: MutableList<Recipe>
    )

    @ConfigSerializable
    data class Recipe(
        val key: String,
        @Setting("result_amount")
        val resultAmount: Int,
        val ingredients: Map<String, Int>,
        val time: String,
        @Setting("level_needed")
        val levelNeeded: Int,
        @Setting("xp_given")
        val xpGiven: Int
    )
}
