package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseType {
    COTTAGE("Cottage", BuildingType.COTTAGE),
    CAPE_COD_COTTAGE("Cape Cod Cottage", BuildingType.CAPE_COD_COTTAGE),
    CHALET_BUNGALOW("Chalet Bungalow", BuildingType.CHALET_BUNGALOW),
    CONCH_HOUSE("Conch House", BuildingType.CONCH_HOUSE),
    BUNGALOW("Bungalow", BuildingType.BUNGALOW);
    private final String name;
    private final BuildingType buildingType;
}
