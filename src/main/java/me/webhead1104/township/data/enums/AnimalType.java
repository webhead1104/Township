package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

@Getter
public enum AnimalType {
    COWSHED_1("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), ItemType.COW_FEED, ItemType.MILK, 3, Duration.ofSeconds(20)),
    COWSHED_2("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), ItemType.COW_FEED, ItemType.MILK, 3, Duration.ofMinutes(20)),
    COWSHED_3("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), ItemType.COW_FEED, ItemType.MILK, 3, Duration.ofMinutes(20)),
    CHICKEN_COOP_1("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), ItemType.CHICKEN_FEED, ItemType.EGG, 4, Duration.ofHours(1)),
    CHICKEN_COOP_2("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), ItemType.CHICKEN_FEED, ItemType.EGG, 4, Duration.ofHours(1)),
    CHICKEN_COOP_3("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), ItemType.CHICKEN_FEED, ItemType.EGG, 4, Duration.ofHours(1));
    private final String name;
    private final Component menuTitle;
    @Getter(value = lombok.AccessLevel.NONE)
    private final ItemStack animalItemStack;
    private final ItemType feedType;
    private final ItemType productType;
    private final int xpGivenOnClaim;
    private final Duration timeTakesToFeed;

    AnimalType(String name, ItemStack animalItemStack, ItemType feedType, ItemType productType, int xpGivenOnClaim, Duration timeTakesToFeed) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.animalItemStack = animalItemStack;
        this.feedType = feedType;
        this.productType = productType;
        this.xpGivenOnClaim = xpGivenOnClaim;
        this.timeTakesToFeed = timeTakesToFeed;
    }

    public ItemStack getAnimalItemStack() {
        return animalItemStack.clone();
    }
}
