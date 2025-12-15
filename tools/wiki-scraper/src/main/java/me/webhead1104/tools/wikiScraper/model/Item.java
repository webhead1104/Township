package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Slf4j
@Getter
@Setter
@ConfigSerializable
@AllArgsConstructor
public class Item {
    private String key;
    private String material;
    @Setting("level_needed")
    private int levelNeeded;
    @Setting("sell_price")
    private int sellPrice;
    @Setting("base_weight")
    private int baseWeight;

    public Item(String key, String material, int levelNeeded, int sellPrice) {
        this.key = key;
        this.material = material;
        this.levelNeeded = levelNeeded;
        this.sellPrice = sellPrice;
        this.baseWeight = calculateWeight(levelNeeded, sellPrice);
    }

    private static int calculateWeight(int unlockLevel, int sellPrice) {
        if (unlockLevel == -1 || sellPrice == -1) {
            return -1;
        }
        int baseWeight;
        if (unlockLevel <= 3) {
            baseWeight = 50;
        } else if (unlockLevel <= 10) {
            baseWeight = 40;
        } else if (unlockLevel <= 20) {
            baseWeight = 30;
        } else if (unlockLevel <= 30) {
            baseWeight = 20;
        } else {
            baseWeight = 10;
        }

        int priceModifier;
        if (sellPrice < 10) {
            priceModifier = 5;
        } else if (sellPrice < 50) {
            priceModifier = 0;
        } else if (sellPrice < 100) {
            priceModifier = -5;
        } else if (sellPrice < 200) {
            priceModifier = -10;
        } else {
            priceModifier = -15;
        }

        return Math.max(5, baseWeight + priceModifier);
    }
}