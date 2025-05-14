package me.webhead1104.township.data.serializers;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerializer implements TypeSerializer<Instant> {

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