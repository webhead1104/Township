package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.data.impls.CoinPrice;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.ConstructionMaterials;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum BuildingType {
    //houses
    COTTAGE(List.of(
            new Building(4, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(4, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(4, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(5, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), 5, TileSize.SIZE_1X1, "COTTAGE")
    ), "COTTAGE"),
    CAPE_COD_COTTAGE(List.of(
            new Building(1, 10, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(5, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(6, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(7, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(8, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE")
    ), "CAPE_COD_COTTAGE"),

    CHALET_BUNGALOW(List.of(
            new Building(1, 15, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(1, 15, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(5, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(6, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(9, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW")
    ), "CHALET_BUNGALOW"),

    CONCH_HOUSE(List.of(
            new Building(1, 20, 0, new CoinPrice(0), null, null, 20, TileSize.SIZE_1X1, "CONCH_HOUSE"),
            new Building(7, 20, 0, new CoinPrice(20), null, Duration.ofMinutes(30), 20, TileSize.SIZE_1X1, "CONCH_HOUSE")
    ), "CONCH_HOUSE"),

    BUNGALOW(List.of(
            new Building(8, 25, 0, new CoinPrice(25), null, Duration.ofHours(1), 25, TileSize.SIZE_1X1, "BUNGALOW"),
            new Building(9, 25, 0, new CoinPrice(25), null, Duration.ofHours(1), 25, TileSize.SIZE_1X1, "BUNGALOW")
    ), "BUNGALOW"),

    //community
    GROCERY_STORE(List.of(
            new Building(5, 0, 10, new CoinPrice(25), new ConstructionMaterials(0, 1, 0), Duration.ofMinutes(2), 20, TileSize.SIZE_2X1, "GROCERY_STORE")
    ), "GROCERY_STORE"),

    SCHOOL(List.of(
            new Building(5, 0, 20, new CoinPrice(30), new ConstructionMaterials(1, 1, 0), Duration.ofMinutes(10), 40, TileSize.SIZE_2X1, "SCHOOL")
    ), "SCHOOL"),

    HOSPITAL(List.of(
            new Building(6, 0, 25, new CoinPrice(40), new ConstructionMaterials(1, 1, 1), Duration.ofMinutes(20), 50, TileSize.SIZE_2X1, "HOSPITAL")
    ), "HOSPITAL"),

    POLICE_STATION(List.of(
            new Building(7, 0, 30, new CoinPrice(50), new ConstructionMaterials(2, 1, 1), Duration.ofMinutes(30), 60, TileSize.SIZE_2X1, "POLICE_STATION")
    ), "POLICE_STATION"),

    FIRE_STATION(List.of(
            new Building(8, 0, 35, new CoinPrice(75), new ConstructionMaterials(2, 2, 1), Duration.ofHours(1), 70, TileSize.SIZE_2X1, "FIRE_STATION")
    ), "FIRE_STATION"),

    POST_OFFICE(List.of(
            new Building(9, 0, 40, new CoinPrice(100), new ConstructionMaterials(2, 1, 3), Duration.ofHours(1).plusMinutes(30), 80, TileSize.SIZE_2X1, "POST_OFFICE")
    ), "POST_OFFICE"),

    //factories
    BAKERY(List.of(
            new Building(2, 55, 0, new CoinPrice(125), null, Duration.ofSeconds(3), 10, TileSize.SIZE_2X2, "BAKERY")
    ), "BAKERY"),

    FEED_MILL(List.of(
            new Building(3, 60, 0, new CoinPrice(150), null, Duration.ofSeconds(40), 11, TileSize.SIZE_2X2, "FEED_MILL")
    ), "FEED_MILL"),

    DAIRY_FACTORY(List.of(
            new Building(4, 65, 0, new CoinPrice(175), null, Duration.ofHours(1), 12, TileSize.SIZE_2X2, "DAIRY_FACTORY")
    ), "DAIRY_FACTORY"),

    SUGAR_FACTORY(List.of(
            new Building(7, 75, 0, new CoinPrice(250), null, Duration.ofHours(2), 15, TileSize.SIZE_2X2, "SUGAR_FACTORY")
    ), "SUGAR_FACTORY"),

    TEXTILE_FACTORY(List.of(
            new Building(9, 105, 0, new CoinPrice(500), null, Duration.ofHours(6), 25, TileSize.SIZE_2X2, "TEXTILE_FACTORY")
    ), "TEXTILE_FACTORY"),

    //farming
    COWSHED(List.of(
            new Building(1, 0, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_2X2, "COWSHED")
    ), "COWSHED"),

    CHICKEN_COOP(List.of(
            new Building(5, 0, 0, new CoinPrice(200), null, Duration.ofHours(1), 13, TileSize.SIZE_2X2, "CHICKEN_COOP")
    ), "CHICKEN_COOP"),

    //special
    BARN(List.of(
            new Building(1, 0, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_3X3, "BARN")
    ), "BARN"),

    HELICOPTER(List.of(
            new Building(3, 0, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_3X3, "HELICOPTER")
    ), "HELICOPTER"),

    TRAIN(List.of(
            new Building(5, 0, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_1X3, "TRAIN")
    ), "TRAIN"),

    TOWN_HALL(List.of(
            new Building(6, 0, 0, new CoinPrice(0), null, null, 0, TileSize.SIZE_2X2, "TOWN_HALL")
    ), "TOWN_HALL");
    private final Map<Integer, Building> buildings = new HashMap<>();
    private final String ID;

    BuildingType(List<Building> buildings, String ID) {
        int i = 0;
        for (Building building : buildings) {
            this.buildings.put(i++, building);
        }
        this.ID = ID;
    }
}
