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
}
