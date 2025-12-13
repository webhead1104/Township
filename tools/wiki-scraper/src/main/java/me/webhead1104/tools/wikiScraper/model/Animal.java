package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@Setter
@ConfigSerializable
@AllArgsConstructor
public class Animal {
    private String key;
    private int amount;
    private String name;
    @Setting("animal_name")
    private String animalName;
    private String material;
    private String feed;
    private String product;
    @Setting("xp_claim")
    private int xpClaim;
    private String time;
    @Setting("building_key")
    private String buildingKey;
    private transient int levelNeeded;
}
