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
package me.webhead1104.towncraft.wikiScraper.utils

import io.leangen.geantyref.TypeToken
import me.webhead1104.towncraft.wikiScraper.data.Tile
import me.webhead1104.towncraft.wikiScraper.data.TileSize
import me.webhead1104.towncraft.wikiScraper.serializers.TileSerializer
import me.webhead1104.towncraft.wikiScraper.serializers.TileSizeSerializer
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.Normalizer
import java.util.*

class Utils {
    companion object {
        val LOADER: GsonConfigurationLoader.Builder = GsonConfigurationLoader.builder()
            .defaultOptions { opts ->
                opts.shouldCopyDefaults(true).serializers { builder ->
                    builder.register<Tile>(Tile::class.java, TileSerializer())
                    builder.register<TileSize>(TileSize::class.java, TileSizeSerializer())
                }
            }

        fun <T> saveJson(data: List<T>, outDir: File, fileName: String, elementType: Class<T>): File {
            if (!outDir.exists() && !outDir.mkdirs()) {
                throw IOException("Failed to create output directory: $outDir")
            }
            val file = File(outDir, fileName)
            LOADER.file(file).build().save(LOADER.build().createNode().setList<T>(elementType, data))
            return file
        }

        fun <T> readJson(path: String, elementType: Class<T>): T {
            return readJson(path, TypeToken.get(elementType))
        }

        fun <T> readJson(path: String, elementType: TypeToken<T>): T {
            val stream = Utils::class.java.getResourceAsStream(path)
                ?: throw IOException("File does not exist at path: $path")

            stream.use { stream ->
                val loader = LOADER.source { BufferedReader(InputStreamReader(stream)) }.build()
                val data: T = loader.load().get(elementType)
                    ?: throw IOException("Data is null - could not deserialize to ${elementType.type}")

                return data
            }
        }

        fun normalizeForKey(itemName: String): String {
            var normalized = itemName.lowercase(Locale.getDefault())

            // Remove accents/diacritics (é -> e, ê -> e, etc.)
            normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
            normalized = normalized.replace("\\p{M}".toRegex(), "") // Remove diacritical marks

            normalized = normalized.replace(" ".toRegex(), "_")
            normalized = normalized.replace("-".toRegex(), "_")
            normalized = normalized.replace("'".toRegex(), "")
            normalized = normalized.replace("_x3".toRegex(), "")

            normalized = fix(normalized)
            return normalized
        }

        fun fix(string: String): String {
            return when (string) {
                "carrots" -> "carrot"
                "cookie" -> "cookies"
                "strawberries" -> "strawberry"
                "honeycomb", "honey" -> "honeycombs"
                "grape" -> "grapes"
                "olive" -> "olives"
                "eggs" -> "egg"
                "coconuts" -> "coconut"
                "colorful_feathers" -> "colorful_feather"
                "roses" -> "rose"
                "mushrooms" -> "mushroom"
                "peanut_plants" -> "peanut_plant"
                "corn_chip" -> "corn_chips"
                "tea_plants" -> "tea_plant"
                "tea_bag" -> "tea_bags"
                "bronze_ores", "bronze_ore" -> "copper_ore"
                "silver_ores" -> "silver_ore"
                "gold_ores" -> "gold_ore"
                "platinum_ores" -> "platinum_ore"
                else -> string
            }
        }
    }
}