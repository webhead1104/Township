package me.webhead1104.tools.wikiScraper.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.scrapers.ItemScraper;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class Item {
    private String key;
    private String material;
    @SerializedName("level_needed")
    private int levelNeeded;
    @SerializedName("sell_price")
    private int sellPrice;

    public Item(String key, int levelNeeded, int sellPrice) {
        this.key = key.replaceAll(" ", "_").replaceAll("_x3", "").replaceAll("'", "").toLowerCase();
        this.material = ItemScraper.ITEM_NAMES.getOrDefault(this.key, "minecraft:player_head");
        this.levelNeeded = levelNeeded;
        this.sellPrice = sellPrice;
    }
}
