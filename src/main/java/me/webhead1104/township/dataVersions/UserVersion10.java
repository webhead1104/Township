package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.tiles.PlotTile;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion10 implements DataVersion {
    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                DataVersionUtils.replaceBuilding(new PlotTile(new Plot())),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Township.key("plot")), Township.key("plot"), 0)
        );
    }

    @Override
    public int getVersion() {
        return 10;
    }
}
