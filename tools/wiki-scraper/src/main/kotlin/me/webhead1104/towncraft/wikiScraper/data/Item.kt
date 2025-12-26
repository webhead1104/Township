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
package me.webhead1104.towncraft.wikiScraper.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import kotlin.math.max

@ConfigSerializable
data class Item(
    val key: String,
    val material: String,
    @Setting("level_needed") val levelNeeded: Int,
    @Setting("sell_price") val sellPrice: Int,
    @Setting("base_weight") var baseWeight: Int = calculateWeight(levelNeeded, sellPrice),
    @Transient var factoryData: FactoryData? = null
) {
    data class FactoryData(
        val factoryName: String,
        val itemName: String,
        val ingredients: Map<String, Int>,
        val xpGiven: Int,
        val time: String
    )

    companion object {
        private fun calculateWeight(unlockLevel: Int, sellPrice: Int): Int {
            if (unlockLevel == -1 || sellPrice == -1) {
                return -1
            }
            val baseWeight: Int = if (unlockLevel <= 3) {
                50
            } else if (unlockLevel <= 10) {
                40
            } else if (unlockLevel <= 20) {
                30
            } else if (unlockLevel <= 30) {
                20
            } else {
                10
            }
            val priceModifier: Int = if (sellPrice < 10) {
                5
            } else if (sellPrice < 50) {
                0
            } else if (sellPrice < 100) {
                -5
            } else if (sellPrice < 200) {
                -10
            } else {
                -15
            }

            return max(5, baseWeight + priceModifier)
        }
    }
}