package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion6 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return node -> node.node("expansions-purchased").raw(0);
    }

    @Override
    public int getVersion() {
        return 6;
    }
}
