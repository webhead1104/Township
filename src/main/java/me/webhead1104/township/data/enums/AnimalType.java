package me.webhead1104.township.data.enums;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

@Getter
public enum AnimalType {
    COWSHED_1("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 0, 1, 0, Duration.ofMinutes(20)),
    COWSHED_2("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 1000, 15, 46, Duration.ofMinutes(20)),
    COWSHED_3("Cowshed", cowItemStack(), ItemType.COW_FEED, ItemType.MILK, 3, 5000, 22, 211, Duration.ofMinutes(20)),
    CHICKEN_COOP_1("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 200, 5, 13, Duration.ofHours(1)),
    CHICKEN_COOP_2("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 3000, 24, 128, Duration.ofHours(1)),
    CHICKEN_COOP_3("Chicken Coop", chickenItemStack(), ItemType.CHICKEN_FEED, ItemType.EGG, 4, 15000, 39, 623, Duration.ofHours(1));
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
    private final Duration timeTakesToFeed;

    AnimalType(String name, ItemStack animalItemStack, ItemType feedType, ItemType productType, int xpGivenOnClaim, int coinsNeeded, int levelNeeded, int xpGivenOnBuild, Duration timeTakesToFeed) {
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
