package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.PurchasedBuildings;
import me.webhead1104.towncraft.tiles.BarnTile;
import me.webhead1104.towncraft.tiles.TrainTile;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion11 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                DataVersionUtils.replaceBuilding(new BarnTile()),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Towncraft.key("barn"))),
                DataVersionUtils.replaceBuilding(new TrainTile()),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Towncraft.key("train"))),
                DataVersionUtils.replaceBuildingMatchingClass("PlotTile"),
                DataVersionUtils.setPurchasedBuilding(new PurchasedBuildings.PurchasedBuilding(0, -1, false, Towncraft.key("plot")))
        );
    }

    @Override
    public int getVersion() {
        return 11;
    }
}