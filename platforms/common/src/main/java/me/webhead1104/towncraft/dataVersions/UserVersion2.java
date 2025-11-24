package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.transformation.ConfigurationTransformation.WILDCARD_OBJECT;

@Keep
public final class UserVersion2 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInChildren(rootNode, node -> {
            node.node("give-item-amount").raw(node.node("give-item", "amount").getInt());
            node.node("give-item-type").raw(node.node("give-item", "type").getInt());

            node.node("claim-item-amount").raw(node.node("claim-item", "amount").getInt());
            node.node("claim-item-type").raw(node.node("claim-item", "type").getInt());
        }, "trains", "trains", WILDCARD_OBJECT, "train-cars", WILDCARD_OBJECT);
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
