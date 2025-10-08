package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@Keep
public final class UserVersion5 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                node ->
                        node.node(path("animals", "animal-buildings")).childrenMap().forEach((buildingKey, buildingNode) ->
                                buildingNode.node("unlocked").raw(null)),

                node -> node.node(path("factories", "factory-buildings")).childrenMap()
                        .forEach((buildingKey, buildingNode) -> buildingNode.node("unlocked").raw(null)));
    }

    @Override
    public int getVersion() {
        return 5;
    }
}
