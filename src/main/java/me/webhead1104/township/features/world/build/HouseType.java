package me.webhead1104.township.features.world.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.Township;
import net.kyori.adventure.key.Key;

@Getter
@AllArgsConstructor
public enum HouseType {
    COTTAGE("Cottage", Township.key("cottage")),
    CAPE_COD_COTTAGE("Cape Cod Cottage", Township.key("cape_cod_cottage")),
    CHALET_BUNGALOW("Chalet Bungalow", Township.key("chalet_bungalow")),
    CONCH_HOUSE("Conch House", Township.key("conch_house")),
    BUNGALOW("Bungalow", Township.key("bungalow"));
    private final String name;
    private final Key buildingType;
}
