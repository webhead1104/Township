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
import me.webhead1104.towncraft.price.CoinPrice;
import me.webhead1104.towncraft.price.Price;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class PriceSerializer extends TowncraftSerializer<Price> {
    @Override
    public Price deserialize(@NotNull Type t, @NotNull ConfigurationNode node)
            throws SerializationException {
        if (node.node("type").virtual()) {
            throw new SerializationException("Cannot deserialize Price");
        }
        String type = node.node("type").getString();
        if (type == null) {
            throw new RuntimeException("Price not found!");
        }
        if (type.equals("coin")) {
            return new CoinPrice(node.node("amount").getInt());
        }
        throw new RuntimeException("Price not found!");
    }

    @Override
    public void serialize(
            @NotNull Type type, @Nullable Price price, @NotNull ConfigurationNode node)
            throws SerializationException {
        if (price == null) {
            throw new SerializationException("Price is null");
        }

        if (price instanceof CoinPrice(int amount)) {
            node.node("type").set("coin");
            node.node("amount").set(amount);
        }
    }

    @Override
    public Class<Price> getType() {
        return Price.class;
    }
}
