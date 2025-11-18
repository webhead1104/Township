package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion12 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInAllWorldTiles(rootNode, plotNode -> {
            if (!"PlotTile".equals(plotNode.node("class").getString())) {
                return;
            }
            ConfigurationNode node = plotNode.node("properties");
            node.node("plotType").raw(node.node("plot", "plotType").getString());
            node.node("instant").raw(node.node("plot", "instant").getString());
            node.node("claimable").raw(node.node("plot", "claimable").getString());
        });
    }

    @Override
    public int getVersion() {
        return 12;
    }
}