package me.webhead1104.township.dataVersions.dataVersions;

import me.webhead1104.township.dataVersions.DataVersion;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.time.Instant;
import java.util.Map;

@SuppressWarnings("unused")
public final class UserVersion1 implements DataVersion {

    private static void injectInstantInPlotTiles(ConfigurationNode node) throws SerializationException {
        if (node.isMap()) {
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
                ConfigurationNode child = entry.getValue();

                if ("me.webhead1104.township.tiles.tiles.PlotTile".equals(child.node("class").getString())) {
                    ConfigurationNode plotNode = child.node("properties", "plot");
                    if (!plotNode.virtual() && plotNode.node("instant").virtual()) {
                        plotNode.node("instant").set(Instant.EPOCH);
                    }
                    if (!plotNode.virtual() && plotNode.node("claimable").virtual()) {
                        plotNode.node("claimable").set(false);
                    }
                }
                injectInstantInPlotTiles(child);
            }
        }
    }

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(NodePath.path(), (path, node) -> {
                    injectInstantInPlotTiles(node);
                    return null;
                }).build();
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
