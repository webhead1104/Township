package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseType {
    COTTAGE("Cottage"),
    CAPE_COD_COTTAGE("Cape Cod Cottage"),
    CHALET_BUNGALOW("Chalet Bungalow"),
    CONCH_HOUSE("Conch House"),
    BUNGALOW("Bungalow");
    private final String name;
}
