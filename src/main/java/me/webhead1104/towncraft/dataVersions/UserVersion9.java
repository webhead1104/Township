package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion9 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return rootNode -> runInChildren(rootNode, node -> {
            if (node.getInt() == 0) {
                node.raw(null);
            }
        }, "barn", "item-map");
    }

    @Override
    public int getVersion() {
        return 9;
    }
}
