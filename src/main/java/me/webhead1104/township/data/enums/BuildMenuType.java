package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

import java.util.List;

@Getter
@AllArgsConstructor
public enum BuildMenuType {
    HOUSING(Msg.format("Housing"),
            List.of(BuildingType.COTTAGE, BuildingType.CAPE_COD_COTTAGE, BuildingType.CHALET_BUNGALOW, BuildingType.CONCH_HOUSE, BuildingType.BUNGALOW)),
    COMMUNITY(Msg.format("Community Buildings"),
            List.of(BuildingType.GROCERY_STORE, BuildingType.SCHOOL, BuildingType.HOSPITAL, BuildingType.POLICE_STATION, BuildingType.FIRE_STATION, BuildingType.POST_OFFICE)),
    FACTORIES(Msg.format("Factories"),
            List.of(BuildingType.BAKERY, BuildingType.FEED_MILL, BuildingType.DAIRY_FACTORY, BuildingType.SUGAR_FACTORY)),
    FARMING(Msg.format("Farming"),
            List.of(BuildingType.COWSHED, BuildingType.CHICKEN_COOP)),
    SPECIAL(Msg.format("Special"),
            List.of(BuildingType.BARN, BuildingType.HELICOPTER, BuildingType.TRAIN, BuildingType.TOWN_HALL));
    private final Component menuTitle;
    private final List<BuildingType> buildings;
}
