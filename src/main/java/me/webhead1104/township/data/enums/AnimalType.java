package me.webhead1104.township.data.enums;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum AnimalType {
    //todo add custom tile in world menu
    COWSHED_1("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 0, 1, 0, 10),
    COWSHED_2("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 1000, 15, 46, 10),
    COWSHED_3("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 5000, 22, 211, 10),
    CHICKEN_COOP_1("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 200, 5, 13, 3600),
    CHICKEN_COOP_2("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 3000, 24, 128, 3600),
    CHICKEN_COOP_3("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 15000, 39, 623, 3600);
    private final String name;
    private final Component menuTitle;
    @Getter(value = lombok.AccessLevel.NONE)
    private final ItemStack animalItemStack;
    private final ItemType feedType;
    private final ItemType productType;
    private final int xpGivenOnClaim;
    private final int coinsNeeded;
    private final int levelNeeded;
    private final int xpGivenOnBuild;
    //in seconds
    private final long timeTakesToFeed;

    AnimalType(String name, ItemStack animalItemStack, ItemType feedType, ItemType productType, int xpGivenOnClaim, int coinsNeeded, int levelNeeded, int xpGivenOnBuild, long timeTakesToFeed) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.animalItemStack = animalItemStack;
        this.feedType = feedType;
        this.productType = productType;
        this.xpGivenOnClaim = xpGivenOnClaim;
        this.coinsNeeded = coinsNeeded;
        this.levelNeeded = levelNeeded;
        this.xpGivenOnBuild = xpGivenOnBuild;
        this.timeTakesToFeed = timeTakesToFeed;
    }

    private static ItemStack cowItemStack() {
        ItemStack itemStack = ItemStack.of(Material.COW_SPAWN_EGG);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Cow"));
        return itemStack;
    }

    private static ItemStack chickenItemStack() {
        ItemStack itemStack = ItemStack.of(Material.CHICKEN_SPAWN_EGG);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Chicken"));
        return itemStack;
    }

    public ItemStack getAnimalItemStack() {
        return animalItemStack.clone();
    }
}
