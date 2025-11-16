package me.webhead1104.towncraft.dataVersions;

import me.webhead1104.towncraft.Towncraft;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

public interface DataVersion {
    ConfigurationTransformation getTransformation();

    int getVersion();


    default ConfigurationNode createNode() {
        return Towncraft.GSON_CONFIGURATION_LOADER.build().createNode();
    }
}
