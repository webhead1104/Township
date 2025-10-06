package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@Keep
public final class UserVersion4 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path("world", "world-map"), (path, worldMapNode) -> {
                    worldMapNode.childrenMap().forEach((key, slotMapNode) -> {
                        slotMapNode.node(path("slot-map")).childrenMap().forEach((key2, tileNode) -> {
                            try {
                                tileNode.node("class").set(tileNode.node("class").getString("unknown class").split("me.webhead1104.township.tiles.tiles.")[1]);
                            } catch (SerializationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    });
                    return null;
                }).build();
    }

    @Override
    public int getVersion() {
        return 4;
    }
}
