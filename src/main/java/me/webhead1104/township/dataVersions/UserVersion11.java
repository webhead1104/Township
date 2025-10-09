package me.webhead1104.township.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.tiles.BarnTile;
import me.webhead1104.township.tiles.TrainTile;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion11 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                DataVersionUtils.replaceBuilding(new BarnTile()),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Township.key("barn"))),
                DataVersionUtils.replaceBuilding(new TrainTile()),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Township.key("train"))),
                DataVersionUtils.replaceBuildingMatchingClass("PlotTile"),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Township.key("plot")))
        );
    }

    @Override
    public int getVersion() {
        return 11;
    }
}