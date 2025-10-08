package me.webhead1104.township.serializers;

import me.webhead1104.township.price.CoinPrice;
import me.webhead1104.township.price.Price;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class PriceSerializer implements TypeSerializer<Price> {
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

        if (price instanceof CoinPrice coinPrice) {
            node.node("type").set("coin");
            node.node("amount").set(coinPrice.getAmount());
        }
    }
}
