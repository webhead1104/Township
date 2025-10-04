package me.webhead1104.township.features.animals;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

@Getter
public enum AnimalType {
    COWSHED_1("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), Township.key("cow_feed"), Township.key("milk"), 3, Duration.ofSeconds(20)),
    COWSHED_2("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), Township.key("cow_feed"), Township.key("milk"), 3, Duration.ofMinutes(20)),
    COWSHED_3("Cowshed", Utils.getItemStack("Cow", Material.COW_SPAWN_EGG), Township.key("cow_feed"), Township.key("milk"), 3, Duration.ofMinutes(20)),
    CHICKEN_COOP_1("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), Township.key("chicken_feed"), Township.key("egg"), 4, Duration.ofHours(1)),
    CHICKEN_COOP_2("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), Township.key("chicken_feed"), Township.key("egg"), 4, Duration.ofHours(1)),
    CHICKEN_COOP_3("Chicken Coop", Utils.getItemStack("Chicken", Material.COW_SPAWN_EGG), Township.key("chicken_feed"), Township.key("egg"), 4, Duration.ofHours(1));
    private final String name;
    private final Component menuTitle;
    private final ItemStack animalItemStack;
    private final ItemType.Item feedType;
    private final ItemType.Item productType;
    private final int xpGivenOnClaim;
    private final Duration timeTakesToFeed;

    AnimalType(String name, ItemStack animalItemStack, Key feedType, Key productType, int xpGivenOnClaim, Duration timeTakesToFeed) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.animalItemStack = animalItemStack;
        this.feedType = ItemType.get(feedType);
        this.productType = ItemType.get(productType);
        this.xpGivenOnClaim = xpGivenOnClaim;
        this.timeTakesToFeed = timeTakesToFeed;
    }

    public ItemStack getAnimalItemStack() {
        return animalItemStack.clone();
    }
}
