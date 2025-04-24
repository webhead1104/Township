package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.inventory.ItemStack;

@Getter
public enum WorldTileType {
    GRASS(MenuItems.World.grass, "grass"),
    STONE(MenuItems.World.stone, "stone"),
    SAND(MenuItems.World.sand, "sand"),
    WATER(MenuItems.World.water, "water"),
    ROAD(MenuItems.World.road, "road"),
    WOOD_PLANK(MenuItems.World.wood_plank, "wood_plank"),
    EXPANSION(MenuItems.World.expansion, "expansion"),
    COWSHED_1(MenuItems.World.cowshed, AnimalType.COWSHED_1, "cowshed_1"),
    COWSHED_2(MenuItems.World.cowshed, AnimalType.COWSHED_2, "cowshed_2"),
    COWSHED_3(MenuItems.World.cowshed, AnimalType.COWSHED_3, "cowshed_3"),
    CHICKEN_COOP_1(MenuItems.World.chicken_coop, AnimalType.CHICKEN_COOP_1, "chicken_coop_1"),
    CHICKEN_COOP_2(MenuItems.World.chicken_coop, AnimalType.CHICKEN_COOP_2, "chicken_coop_2"),
    CHICKEN_COOP_3(MenuItems.World.chicken_coop, AnimalType.CHICKEN_COOP_3, "chicken_coop_3"),
    BAKERY(MenuItems.World.bakery, FactoryType.BAKERY, "bakery"),
    FEED_MILL_1(MenuItems.World.feed_mill, FactoryType.FEED_MILL_1, "feed_mill_1"),
    FEED_MILL_2(MenuItems.World.feed_mill, FactoryType.FEED_MILL_2, "feed_mill_2"),
    FEED_MILL_3(MenuItems.World.feed_mill, FactoryType.FEED_MILL_3, "feed_mill_3"),
    DAIRY_FACTORY(MenuItems.World.dairy, FactoryType.DAIRY_FACTORY, "dairy"),
    SUGAR_FACTORY(MenuItems.World.sugar, FactoryType.SUGAR_FACTORY, "sugar"),
    PLOT(MenuItems.World.plot, "plot"),
    BARN(MenuItems.World.barn, "barn"),
    TRAIN(MenuItems.World.train, "train");
    private final ItemStack item;
    private final String id;
    private final AnimalType animalType;
    private final FactoryType factoryType;

    WorldTileType(ItemStack item, String id) {
        this.item = item;
        this.animalType = null;
        this.factoryType = null;
        this.id = id;
    }

    WorldTileType(ItemStack item, AnimalType type, String id) {
        this.item = item;
        this.animalType = type;
        this.factoryType = null;
        this.id = id;
    }

    WorldTileType(ItemStack item, FactoryType type, String id) {
        this.item = item;
        this.factoryType = type;
        this.animalType = null;
        this.id = id;
    }
}
