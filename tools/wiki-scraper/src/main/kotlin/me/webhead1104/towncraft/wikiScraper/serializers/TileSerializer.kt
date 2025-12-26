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
package me.webhead1104.towncraft.wikiScraper.serializers

import me.webhead1104.towncraft.wikiScraper.data.Tile
import org.apache.commons.lang3.NotImplementedException
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.Type

class TileSerializer : TypeSerializer<Tile> {
    override fun deserialize(type: Type?, node: ConfigurationNode?): Tile? {
        throw NotImplementedException("Not implemented")
    }

    override fun serialize(type: Type?, obj: Tile?, node: ConfigurationNode) {
        if (obj == null) {
            throw SerializationException("Cannot serialize a null Tile!")
        }

        val tileClass: Class<*> = obj.javaClass
        node.node(CLASS_KEY).set(
            tileClass.getName().split("me.webhead1104.towncraft.wikiScraper.data.".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        )

        val propertiesNode = node.node(PROPERTIES_KEY)

        try {
            var hasNonNullFields = false
            for (field in collectAllFields(tileClass)) {
                if (Modifier.isStatic(field.modifiers)) continue
                field.setAccessible(true)
                val fieldName = field.name
                val value = field.get(obj)
                if (value != null) {
                    propertiesNode.node(fieldName).set(field.genericType, value)
                    hasNonNullFields = true
                }
            }

            if (!hasNonNullFields) {
                node.removeChild(PROPERTIES_KEY)
            }
        } catch (e: IllegalAccessException) {
            throw SerializationException("Error accessing fields of Tile: " + e.message)
        } catch (e: Exception) {
            throw SerializationException("Error serializing Tile: " + e.message)
        }
    }

    companion object {
        private const val CLASS_KEY = "class"
        private const val PROPERTIES_KEY = "properties"

        private fun collectAllFields(clazz: Class<*>?): MutableList<Field> {
            val hierarchy: MutableList<Class<*>> = ArrayList()
            var current = clazz
            while (current != null && Tile::class.java.isAssignableFrom(current)) {
                hierarchy.add(current)
                current = current.getSuperclass()
            }
            hierarchy.reverse()

            val result: MutableList<Field> = ArrayList()
            for (c in hierarchy) {
                for (f in c.declaredFields) {
                    if (Modifier.isStatic(f.modifiers)) continue
                    result.add(f)
                }
            }
            return result
        }
    }
}