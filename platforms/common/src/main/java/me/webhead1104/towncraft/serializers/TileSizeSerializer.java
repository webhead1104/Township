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
import me.webhead1104.towncraft.data.TileSize;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class TileSizeSerializer extends TowncraftSerializer<TileSize> {
    @Override
    public TileSize deserialize(@NotNull Type type, @NotNull ConfigurationNode node)
            throws SerializationException {
        if (node.virtual()) {
            throw new SerializationException("Cannot deserialize a tile size of a virtual tile");
        }
        if (node.getString() == null) {
            throw new SerializationException("tile size is null");
        }
        String[] size = node.getString().split("x");
        return new TileSize(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
    }

    @Override
    public void serialize(
            @NotNull Type type, @Nullable TileSize tileSize, @NotNull ConfigurationNode node)
            throws SerializationException {
        if (tileSize == null) {
            throw new SerializationException("tile size is null");
        }
        node.set(String.format("%dx%d", tileSize.height(), tileSize.width()));
    }

    @Override
    public Class<TileSize> getType() {
        return TileSize.class;
    }
}
