package me.webhead1104.township.dataVersions;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@SuppressWarnings("unused")
public class UserVersion6 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path(), (path, node) -> {
                    node.node("expansions-purchased").raw(0);
                    return null;
                })
                .addAction(path("world", "world-map"), (path, worldMapNode) -> {
                    worldMapNode.childrenMap().forEach((section, sectionNode) -> sectionNode.node("slot-map").childrenMap().forEach((slot, tile) -> {
                        if (tile.node("class").getString().equals("ExpansionTile")) {
                            tile.node("slot").raw(slot);
                        }
                    }));
                    return null;
                })
                .build();
    }

    @Override
    public int getVersion() {
        return 6;
    }
}
