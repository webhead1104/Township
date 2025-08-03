package me.webhead1104.township.dataVersions.dataVersions;

import me.webhead1104.township.dataVersions.DataVersion;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@SuppressWarnings("unused")
public class UserVersion3 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path(), (path, node) -> {
                    node.node("xp").set(node.node("level", "xp").getInt());
                    node.node("level").set(node.node("level", "level").getInt());
                    return null;
                })
                .build();
    }

    @Override
    public int getVersion() {
        return 3;
    }
}
