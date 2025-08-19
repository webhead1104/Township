package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum BuildMenuType {
    HOUSING(Msg.format("Housing"),
            List.of(BuildingType.COTTAGE, BuildingType.CAPE_COD_COTTAGE, BuildingType.CHALET_BUNGALOW, BuildingType.CONCH_HOUSE, BuildingType.BUNGALOW)),
    COMMUNITY(Msg.format("Community Buildings"),
            List.of(BuildingType.GROCERY_STORE, BuildingType.SCHOOL, BuildingType.HOSPITAL, BuildingType.POLICE_STATION, BuildingType.FIRE_STATION, BuildingType.POST_OFFICE)),
    FACTORIES(Msg.format("Factories"),
            List.of(BuildingType.BAKERY, BuildingType.FEED_MILL, BuildingType.DAIRY_FACTORY, BuildingType.SUGAR_FACTORY, BuildingType.TEXTILE_FACTORY)),
    FARMING(Msg.format("Farming"),
            List.of(BuildingType.COWSHED, BuildingType.CHICKEN_COOP)),
    SPECIAL(Msg.format("Special"),
            List.of(BuildingType.BARN, BuildingType.HELICOPTER, BuildingType.TRAIN, BuildingType.TOWN_HALL));
    private final Component menuTitle;
    private final Map<Integer, List<BuildingType>> buildings = new HashMap<>();

    BuildMenuType(Component menuTitle, List<BuildingType> buildings) {
        this.menuTitle = menuTitle;
        int page = 0;
        int i = 0;
        this.buildings.put(page, new ArrayList<>(6));
        for (BuildingType buildingType : buildings) {
            this.buildings.get(page).add(buildingType);
            if (i == 6) {
                page++;
                i = 0;
                this.buildings.put(page, new ArrayList<>(6));
            }
            i++;
        }
    }

    public List<BuildingType> getBuildings(int page) {
        return this.buildings.get(page);
    }
}
