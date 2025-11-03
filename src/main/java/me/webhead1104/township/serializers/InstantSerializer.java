package me.webhead1104.township.serializers;

import com.google.errorprone.annotations.Keep;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.time.Instant;

@Keep
public class InstantSerializer extends TownshipSerializer<Instant> {

    @Override
    public Instant deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String instant = node.getString();
        if (instant == null) throw new SerializationException("Cannot deserialize a null instant!");
        return Instant.parse(instant);
    }


    @Override
    public void serialize(@NotNull Type type, @Nullable Instant obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Cannot serialize a null instant!");
        node.set(obj.toString());
    }
}