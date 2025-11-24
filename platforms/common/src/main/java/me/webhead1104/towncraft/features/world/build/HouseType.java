package me.webhead1104.towncraft.features.world.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import net.kyori.adventure.key.Key;

@Getter
@AllArgsConstructor
public enum HouseType {
    COTTAGE("Cottage", Towncraft.key("cottage")),
    CAPE_COD_COTTAGE("Cape Cod Cottage", Towncraft.key("cape_cod_cottage")),
    CHALET_BUNGALOW("Chalet Bungalow", Towncraft.key("chalet_bungalow")),
    CONCH_HOUSE("Conch House", Towncraft.key("conch_house")),
    BUNGALOW("Bungalow", Towncraft.key("bungalow"));
    private final String name;
    private final Key buildingType;
}
