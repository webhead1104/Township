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
public class Plot {
    private String key;
    private Price price;
    @Setting("level_needed")
    private int levelNeeded;
    @Setting("xp_given")
    private int xpGiven;
    private String time;
}
