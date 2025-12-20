package me.webhead1104.tools.wikiScraper.serializers;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.tools.wikiScraper.model.tile.TileSize;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@Keep
public class TileSizeSerializer implements TypeSerializer<TileSize> {
    @Override
    public TileSize deserialize(Type type, ConfigurationNode node)
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
    public void serialize(Type type, @Nullable TileSize tileSize, ConfigurationNode node)
            throws SerializationException {
        if (tileSize == null) {
            throw new SerializationException("tile size is null");
        }
        node.set(String.format("%dx%d", tileSize.height(), tileSize.width()));
    }
}
