package me.webhead1104.township.features.world.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.Township;
import net.kyori.adventure.key.Key;

@Getter
@AllArgsConstructor
public enum CommunityBuildingType {
    GROCERY_STORE("Grocery Store", Township.key("grocery_store")),
    SCHOOL("School", Township.key("school")),
    HOSPITAL("Hospital", Township.key("hospital")),
    POLICE_STATION("Police Station", Township.key("police_station")),
    FIRE_STATION("Fire Station", Township.key("fire_station")),
    POST_OFFICE("Post Office", Township.key("post_office"));
    private final String name;
    private final Key buildingType;
}
