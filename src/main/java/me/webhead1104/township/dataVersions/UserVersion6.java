package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

@Keep
public final class UserVersion6 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.builder()
                .addAction(path(), (path, node) -> {
                    node.node("expansions-purchased").raw(0);
                    return null;
                })
                .build();
    }

    @Override
    public int getVersion() {
        return 6;
    }
}
