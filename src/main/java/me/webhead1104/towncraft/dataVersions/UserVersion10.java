package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion10 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                rootNode -> replaceBuilding(rootNode, createNode().node("class").raw("PlotTile")),
                rootNode -> addNotPlacedPurchasedBuilding(rootNode, "plot")
        );
    }

    @Override
    public int getVersion() {
        return 10;
    }
}
