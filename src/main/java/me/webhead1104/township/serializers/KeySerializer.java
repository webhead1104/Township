package me.webhead1104.township.serializers;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class KeySerializer implements TypeSerializer<Key> {

    @Override
    public Key deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String key = node.getString();
        if (key == null) throw new SerializationException("Cannot deserialize a null key!");
        if (key.contains(":")) {
            return Key.key(key.toLowerCase());
        }
        return Key.key("township", key.toLowerCase());
    }


    @Override
    public void serialize(@NotNull Type type, @Nullable Key obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Cannot serialize a null Key!");
        node.set(obj.value().toLowerCase());
    }
}