package me.webhead1104.towncraft.dataVersions;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;

public interface DataVersion {
    ConfigurationTransformation getTransformation();

    int getVersion();
}
