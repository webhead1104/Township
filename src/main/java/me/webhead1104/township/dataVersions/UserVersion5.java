package me.webhead1104.township.dataVersions;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@SuppressWarnings("unused")
public class UserVersion5 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path("animals", "animal-buildings"), (key, node) -> {
                    node.childrenMap().forEach((buildingKey, buildingNode) -> buildingNode.node("unlocked").raw(null));
                    return null;
                }).addAction(path("factories", "factory-buildings"), (key, node) -> {
                    node.childrenMap().forEach((buildingKey, buildingNode) -> buildingNode.node("unlocked").raw(null));
                    return null;
                })
                .build();
    }

    @Override
    public int getVersion() {
        return 5;
    }
}
