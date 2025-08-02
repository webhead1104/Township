package me.webhead1104.township.dataVersions;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;

public interface DataVersion {
    ConfigurationTransformation getTransformation();

    int getVersion();
}
