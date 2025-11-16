package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.PurchasedBuildings;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion10 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                DataVersionUtils.replaceBuildingMatchingClass("PlotTile"),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Towncraft.key("plot")))
        );
    }

    @Override
    public int getVersion() {
        return 10;
    }
}
