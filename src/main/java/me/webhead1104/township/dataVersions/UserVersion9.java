package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion9 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return (rootNode) -> rootNode.node("barn", "item-map").childrenMap().forEach((key, value) -> {
            if (value.getInt(0) == 0) {
                value.raw(null);
            }
        });
    }

    @Override
    public int getVersion() {
        return 9;
    }
}
