package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion4 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInAllWorldTiles(rootNode, node -> {
            ConfigurationNode classNode = node.node("class");
            classNode.raw(classNode.getString("").split("me.webhead1104.towncraft.tiles.tiles.")[1]);
        });
    }

    @Override
    public int getVersion() {
        return 4;
    }
}
