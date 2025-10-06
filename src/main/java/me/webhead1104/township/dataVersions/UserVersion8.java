package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.tiles.BarnTile;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion8 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                DataVersionUtils.replaceBuilding(new BarnTile(), 4),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Township.key("barn")), Township.key("barn"), 0)
        );
    }

    @Override
    public int getVersion() {
        return 8;
    }
}