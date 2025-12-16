package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ConfigSerializable
public class Factory {
    private String key;
    private int amount;
    private String name;
    @Setting("building_key")
    private String buildingKey;
    private List<Recipe> recipes;

    @Getter
    @Setter
    @AllArgsConstructor
    @ConfigSerializable
    public static class Recipe {
        private String key;
        @Setting("result_amount")
        private int resultAmount;
        private Map<String, Integer> ingredients;
        private String time;
        @Setting("level_needed")
        private int levelNeeded;
        @Setting("xp_given")
        private int xpGiven;
    }
}
