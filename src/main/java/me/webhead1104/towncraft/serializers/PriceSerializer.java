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
}
