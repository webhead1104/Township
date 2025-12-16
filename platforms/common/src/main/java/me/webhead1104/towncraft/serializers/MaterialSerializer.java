package me.webhead1104.towncraft.serializers;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class MaterialSerializer extends TowncraftSerializer<TowncraftMaterial> {
    @Override
    public TowncraftMaterial deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String raw = node.getString();
        if (raw == null) {
            throw new SerializationException("Cannot deserialize a null material!");
        }

        return TowncraftMaterialFactory.get(Key.key(raw));
    }


    @Override
    public void serialize(@NotNull Type type, @Nullable TowncraftMaterial material, @NotNull ConfigurationNode node) throws SerializationException {
        if (material == null) {
            throw new SerializationException("Cannot serialize a null material!");
        }

        node.set(material.getKey().asString());
    }

    @Override
    public Class<TowncraftMaterial> getType() {
        return TowncraftMaterial.class;
    }
}
