package me.webhead1104.township.serializers;

import com.google.errorprone.annotations.Keep;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;

@Keep
public class ComponentSerializer extends TownshipSerializer<Component> {

    @Override
    public Component deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String component = node.getString();
        if (component == null) throw new SerializationException("Cannot deserialize a null component!");
        return MiniMessage.miniMessage().deserialize(component);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Component obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Cannot serialize a null component!");
        node.set(MiniMessage.miniMessage().serialize(obj));
    }
}