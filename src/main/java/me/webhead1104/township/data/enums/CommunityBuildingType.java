package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommunityBuildingType {
    GROCERY_STORE("Grocery Store"),
    SCHOOL("School"),
    HOSPITAL("Hospital"),
    POLICE_STATION("Police Station"),
    FIRE_STATION("Fire Station"),
    POST_OFFICE("Post Office");
    private final String name;
}
