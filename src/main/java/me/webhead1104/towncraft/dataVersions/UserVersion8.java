package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion8 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                rootNode -> replaceBuilding(rootNode, createNode().node("class").raw("BarnTile")),
                rootNode -> addNotPlacedPurchasedBuilding(rootNode, "barn")
        );
    }

    @Override
    public int getVersion() {
        return 8;
    }
}