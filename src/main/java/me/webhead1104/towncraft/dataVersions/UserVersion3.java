package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion3 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return node -> {
            node.node("xp").set(node.node("level", "xp").getInt());
            node.node("level").set(node.node("level", "level").getInt());
        };
    }

    @Override
    public int getVersion() {
        return 3;
    }
}
