package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion14 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                rootNode -> replaceBuilding(rootNode, () -> {
                    ConfigurationNode node = createNode();
                    node.node("class").raw("StaticWorldTile");
                    node.node("properties", "material").raw("minecraft:black_concrete");
                    return node;
                }),
                rootNode -> addNotPlacedPurchasedBuilding(rootNode, "helicopter")
        );
    }

    @Override
    public int getVersion() {
        return 14;
    }
}