package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion7 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInAllWorldTiles(rootNode, node -> {
            ConfigurationNode propertiesNode = node.node("properties");
            if ("StaticWorldTile".equals(node.node("class").getString(""))) {
                ConfigurationNode materialNode = propertiesNode.node("material");
                materialNode.raw(Key.key(materialNode.getString("").toLowerCase()).asString());
            }
        });
    }

    @Override
    public int getVersion() {
        return 7;
    }
}
