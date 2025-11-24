package me.webhead1104.towncraft.features.world.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import net.kyori.adventure.key.Key;

@Getter
@AllArgsConstructor
public enum CommunityBuildingType {
    GROCERY_STORE("Grocery Store", Towncraft.key("grocery_store")),
    SCHOOL("School", Towncraft.key("school")),
    HOSPITAL("Hospital", Towncraft.key("hospital")),
    POLICE_STATION("Police Station", Towncraft.key("police_station")),
    FIRE_STATION("Fire Station", Towncraft.key("fire_station")),
    POST_OFFICE("Post Office", Towncraft.key("post_office"));
    private final String name;
    private final Key buildingType;
}
