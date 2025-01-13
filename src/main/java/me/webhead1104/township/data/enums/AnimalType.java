package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;


@Getter
@AllArgsConstructor
public enum AnimalType {

    COWSHED_1(Msg.format("<gold>Cowshed"), MenuItems.cow, ItemType.COW_FEED, ItemType.MILK, WorldTileType.COWSHED_1, 3, 0, 1, 0, 1200, "cowshed_1"),
    COWSHED_2(Msg.format("<gold>Cowshed"), MenuItems.cow, ItemType.COW_FEED, ItemType.MILK, WorldTileType.COWSHED_2, 3, 1000, 15, 46, 1200, "cowshed_2"),
    COWSHED_3(Msg.format("<gold>Cowshed"), MenuItems.cow, ItemType.COW_FEED, ItemType.MILK, WorldTileType.COWSHED_3, 3, 5000, 22, 211, 1200, "cowshed_3"),
    CHICKEN_COOP_1(Msg.format("<gold>Chicken Coop"), MenuItems.chicken, ItemType.CHICKEN_FEED, ItemType.EGG, WorldTileType.CHICKEN_COOP_1, 4, 200, 5, 13, 3600, "chicken_coop_1"),
    CHICKEN_COOP_2(Msg.format("<gold>Chicken Coop"), MenuItems.chicken, ItemType.CHICKEN_FEED, ItemType.EGG, WorldTileType.CHICKEN_COOP_2, 4, 3000, 24, 128, 3600, "chicken_coop_2"),
    CHICKEN_COOP_3(Msg.format("<gold>Chicken Coop"), MenuItems.chicken, ItemType.CHICKEN_FEED, ItemType.EGG, WorldTileType.CHICKEN_COOP_3, 4, 15000, 39, 623, 3600, "chicken_coop_3");
    private final Component menuTitle;
    private final ItemStack animalItemStack;
    private final ItemType feedType;
    private final ItemType productType;
    private final WorldTileType tileType;
    private final int xpGivenOnClaim;
    private final int coinsNeeded;
    private final int levelNeeded;
    private final int xpGivenOnBuild;
    //in seconds
    private final long timeTakesToFeed;
    private final String ID;
}
