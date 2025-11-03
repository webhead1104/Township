package me.webhead1104.township.serializers;

import com.google.errorprone.annotations.Keep;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class MaterialSerializer extends TownshipSerializer<Material> {
    @Override
    public Material deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String raw = node.getString();
        if (raw == null) {
            throw new SerializationException("Cannot deserialize a null material!");
        }

        Key key = Key.key(raw);
        Material material = Registry.MATERIAL.get(key);
        if (material == null) {
            throw new SerializationException("Unknown material: " + raw);
        }

        return material;
    }


    @Override
    public void serialize(@NotNull Type type, @Nullable Material material, @NotNull ConfigurationNode node) throws SerializationException {
        if (material == null) {
            throw new SerializationException("Cannot serialize a null material!");
        }
        Key key = Registry.MATERIAL.getKey(material);
        if (key == null) {
            throw new SerializationException("Unknown material: " + material);
        }
        node.set(key.asString());
    }
}
