/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

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
    private transient FactoryData factoryData;

    public Item(String key, String material, int levelNeeded, int sellPrice,
                FactoryData factoryData) {
        this(key, material, levelNeeded, sellPrice);
        this.factoryData = factoryData;
    }

    public Item(String key, String material, int levelNeeded, int sellPrice, int baseWeight) {
        this(key, material, levelNeeded, sellPrice);
        this.baseWeight = baseWeight;
    }

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

    public record FactoryData(String factoryName, String itemName, Map<String, Integer> ingredients, int xpGiven,
                              String time) {
    }
}