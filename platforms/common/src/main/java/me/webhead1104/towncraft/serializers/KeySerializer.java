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
package me.webhead1104.towncraft.serializers;

import com.google.errorprone.annotations.Keep;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class KeySerializer extends TowncraftSerializer<Key> {

    @Override
    public Key deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String key = node.getString();
        if (key == null) throw new SerializationException("Cannot deserialize a null key!");
        if (key.contains(":")) {
            return Key.key(key.toLowerCase());
        }
        return Key.key("towncraft", key.toLowerCase());
    }


    @Override
    public void serialize(@NotNull Type type, @Nullable Key obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Cannot serialize a null Key!");
        if (obj.namespace().equals("towncraft")) {
            node.set(obj.value().toLowerCase());
        } else {
            node.set(obj.asString());
        }
    }

    @Override
    public Class<Key> getType() {
        return Key.class;
    }
}