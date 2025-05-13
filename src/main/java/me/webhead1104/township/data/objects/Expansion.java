package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.Township;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class Expansion {
    public static final int LATEST_VERSION = 1;
    private int section;
    private int slot;
    private int price;
    private int populationNeeded;

    public static Expansion fromJson(String json) {
        try {
            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.buildAndLoadString(json);
            ConfigurationTransformation configurationTransformation = ConfigurationTransformation.versionedBuilder()
                    .addVersion(LATEST_VERSION, ConfigurationTransformation.builder().build())
                    .build();
            configurationTransformation.apply(node);
            return node.get(Expansion.class);
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst updating a expansion! Please report the following stacktrace to Webhead1104:", e);
        }
        throw new IllegalStateException("An error occurred whilst deserializing a expansion! Please report this to Webhead1104!");
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}
