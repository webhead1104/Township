package me.webhead1104.tools.wikiScraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Slf4j
@Getter
@Setter
@ConfigSerializable
public class Item {
    private String key;
    private String material;
    @Setting("level_needed")
    private int levelNeeded;
    @Setting("sell_price")
    private int sellPrice;

    public Item(String key, String material, int levelNeeded, int sellPrice) {
        this.key = key;
        this.material = material;
        this.levelNeeded = levelNeeded;
        this.sellPrice = sellPrice;
    }
}
