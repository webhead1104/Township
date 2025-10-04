package me.webhead1104.township.dataVersions;

import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@SuppressWarnings("unused")
public class UserDataVersion7 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path("world", "world-map"), (worldPath, worldNode) -> {
                    worldNode.childrenMap().forEach((sectionKey, sectionNode) -> {
                        sectionNode.node("slot-map").childrenMap().forEach((slotKey, slotNode) -> {
                            ConfigurationNode propertiesNode = slotNode.node("properties");
                            switch (slotNode.node("class").getString()) {
                                case "StaticWorldTile": {
                                    ConfigurationNode materialNode = propertiesNode.node("material");
                                    try {
                                        materialNode.set(Key.key(materialNode.getString().toLowerCase()));
                                    } catch (SerializationException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });
                    });
                    return null;
                })
                .build();
    }

    @Override
    public int getVersion() {
        return 7;
    }
}
