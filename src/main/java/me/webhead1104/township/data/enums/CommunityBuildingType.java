package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityBuildingType {
    GROCERY_STORE("Grocery Store", BuildingType.GROCERY_STORE),
    SCHOOL("School", BuildingType.SCHOOL),
    HOSPITAL("Hospital", BuildingType.HOSPITAL),
    POLICE_STATION("Police Station", BuildingType.POLICE_STATION),
    FIRE_STATION("Fire Station", BuildingType.FIRE_STATION),
    POST_OFFICE("Post Office", BuildingType.POST_OFFICE);
    private final String name;
    private final BuildingType buildingType;
}
