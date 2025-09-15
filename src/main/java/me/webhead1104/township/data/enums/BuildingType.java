package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.ConstructionMaterials;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.price.CoinPrice;
import me.webhead1104.township.tiles.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum BuildingType {
    //houses
    COTTAGE(createMap(
            new Building(4, 0, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), new HouseTile(HouseType.COTTAGE), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(4, 0, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), new HouseTile(HouseType.COTTAGE), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(4, 0, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), new HouseTile(HouseType.COTTAGE), 5, TileSize.SIZE_1X1, "COTTAGE"),
            new Building(5, 0, 5, 0, new CoinPrice(5), null, Duration.ofMinutes(1), new HouseTile(HouseType.COTTAGE), 5, TileSize.SIZE_1X1, "COTTAGE")
    )),
    CAPE_COD_COTTAGE(createMap(
            new Building(1, 0, 10, 0, new CoinPrice(0), null, null, new HouseTile(HouseType.CAPE_COD_COTTAGE), 0, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(5, 0, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), new HouseTile(HouseType.CAPE_COD_COTTAGE), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(6, 0, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), new HouseTile(HouseType.CAPE_COD_COTTAGE), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(7, 0, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), new HouseTile(HouseType.CAPE_COD_COTTAGE), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE"),
            new Building(8, 0, 10, 0, new CoinPrice(10), null, Duration.ofMinutes(10), new HouseTile(HouseType.CAPE_COD_COTTAGE), 10, TileSize.SIZE_1X1, "CAPE_COD_COTTAGE")
    )),

    CHALET_BUNGALOW(createMap(
            new Building(1, 0, 15, 0, new CoinPrice(0), null, null, new HouseTile(HouseType.CHALET_BUNGALOW), 0, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(1, 0, 15, 0, new CoinPrice(0), null, null, new HouseTile(HouseType.CHALET_BUNGALOW), 0, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(5, 0, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), new HouseTile(HouseType.CHALET_BUNGALOW), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(6, 0, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), new HouseTile(HouseType.CHALET_BUNGALOW), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW"),
            new Building(9, 0, 15, 0, new CoinPrice(15), null, Duration.ofMinutes(20), new HouseTile(HouseType.CHALET_BUNGALOW), 15, TileSize.SIZE_1X1, "CHALET_BUNGALOW")
    )),

    CONCH_HOUSE(createMap(
            new Building(1, 0, 20, 0, new CoinPrice(0), null, null, new HouseTile(HouseType.CONCH_HOUSE), 20, TileSize.SIZE_1X1, "CONCH_HOUSE"),
            new Building(7, 0, 20, 0, new CoinPrice(20), null, Duration.ofMinutes(30), new HouseTile(HouseType.CONCH_HOUSE), 20, TileSize.SIZE_1X1, "CONCH_HOUSE")
    )),

    BUNGALOW(createMap(
            new Building(8, 0, 25, 0, new CoinPrice(25), null, Duration.ofHours(1), new HouseTile(HouseType.BUNGALOW), 25, TileSize.SIZE_1X1, "BUNGALOW"),
            new Building(9, 0, 25, 0, new CoinPrice(25), null, Duration.ofHours(1), new HouseTile(HouseType.BUNGALOW), 25, TileSize.SIZE_1X1, "BUNGALOW")
    )),

    //community
    GROCERY_STORE(createMap(
            new Building(5, 0, 0, 10, new CoinPrice(25), new ConstructionMaterials(0, 1, 0), Duration.ofMinutes(2), new CommunityBuildingTile(CommunityBuildingType.GROCERY_STORE), 20, TileSize.SIZE_2X1, "GROCERY_STORE")
    )),

    SCHOOL(createMap(
            new Building(5, 0, 0, 20, new CoinPrice(30), new ConstructionMaterials(1, 1, 0), Duration.ofMinutes(10), new CommunityBuildingTile(CommunityBuildingType.SCHOOL), 40, TileSize.SIZE_2X1, "SCHOOL")
    )),

    HOSPITAL(createMap(
            new Building(6, 0, 0, 25, new CoinPrice(40), new ConstructionMaterials(1, 1, 1), Duration.ofMinutes(20), new CommunityBuildingTile(CommunityBuildingType.HOSPITAL), 50, TileSize.SIZE_2X1, "HOSPITAL")
    )),

    POLICE_STATION(createMap(
            new Building(7, 0, 0, 30, new CoinPrice(50), new ConstructionMaterials(2, 1, 1), Duration.ofMinutes(30), new CommunityBuildingTile(CommunityBuildingType.POLICE_STATION), 60, TileSize.SIZE_2X1, "POLICE_STATION")
    )),

    FIRE_STATION(createMap(
            new Building(8, 0, 0, 35, new CoinPrice(75), new ConstructionMaterials(2, 2, 1), Duration.ofHours(1), new CommunityBuildingTile(CommunityBuildingType.FIRE_STATION), 70, TileSize.SIZE_2X1, "FIRE_STATION")
    )),

    POST_OFFICE(createMap(
            new Building(9, 0, 0, 40, new CoinPrice(100), new ConstructionMaterials(2, 1, 3), Duration.ofHours(1).plusMinutes(30), new CommunityBuildingTile(CommunityBuildingType.POST_OFFICE), 80, TileSize.SIZE_2X1, "POST_OFFICE")
    )),

    //factories
    BAKERY(createMap(
            new Building(2, 55, 0, 0, new CoinPrice(125), null, Duration.ofSeconds(3), new FactoryTile(FactoryType.BAKERY), 10, TileSize.SIZE_2X2, "BAKERY")
    )),

    FEED_MILL(createMap(
            new Building(3, 60, 0, 0, new CoinPrice(150), null, Duration.ofSeconds(40), new FactoryTile(FactoryType.FEED_MILL_1), 11, TileSize.SIZE_2X2, "FEED_MILL"),
            new Building(19, 655, 0, 0, new CoinPrice(2800), null, Duration.ofHours(16), new FactoryTile(FactoryType.FEED_MILL_1), 120, TileSize.SIZE_2X2, "FEED_MILL"),
            new Building(44, 60, 0, 0, new CoinPrice(150), null, Duration.ofSeconds(40), new FactoryTile(FactoryType.FEED_MILL_1), 11, TileSize.SIZE_2X2, "FEED_MILL")

    )),

    DAIRY_FACTORY(createMap(
            new Building(4, 65, 0, 0, new CoinPrice(175), null, Duration.ofHours(1), new FactoryTile(FactoryType.BAKERY), 12, TileSize.SIZE_2X2, "DAIRY_FACTORY")
    )),

    SUGAR_FACTORY(createMap(
            new Building(7, 75, 0, 0, new CoinPrice(250), null, Duration.ofHours(2), new FactoryTile(FactoryType.BAKERY), 15, TileSize.SIZE_2X2, "SUGAR_FACTORY")
    )),

    TEXTILE_FACTORY(createMap(
            new Building(9, 105, 0, 0, new CoinPrice(500), null, Duration.ofHours(6), new FactoryTile(FactoryType.BAKERY), 25, TileSize.SIZE_2X2, "TEXTILE_FACTORY")
    )),

    //farming
    COWSHED(createMap(
            new Building(1, 0, 0, 0, new CoinPrice(0), null, null, new AnimalTile(AnimalType.COWSHED_1), 0, TileSize.SIZE_2X2, "COWSHED"),
            new Building(15, 0, 0, 0, new CoinPrice(1000), null, Duration.ofHours(14), new AnimalTile(AnimalType.COWSHED_2), 0, TileSize.SIZE_2X2, "COWSHED"),
            new Building(22, 0, 0, 0, new CoinPrice(5000), null, Duration.ofDays(1).plusHours(2), new AnimalTile(AnimalType.COWSHED_3), 0, TileSize.SIZE_2X2, "COWSHED")
    )),

    CHICKEN_COOP(createMap(
            new Building(5, 0, 0, 0, new CoinPrice(200), null, Duration.ofHours(1), new AnimalTile(AnimalType.CHICKEN_COOP_1), 13, TileSize.SIZE_2X2, "CHICKEN_COOP"),
            new Building(24, 0, 0, 0, new CoinPrice(3000), null, Duration.ofDays(1).plusHours(4), new AnimalTile(AnimalType.CHICKEN_COOP_2), 13, TileSize.SIZE_2X2, "CHICKEN_COOP"),
            new Building(39, 0, 0, 0, new CoinPrice(15000), null, Duration.ofDays(1).plusHours(16), new AnimalTile(AnimalType.CHICKEN_COOP_3), 13, TileSize.SIZE_2X2, "CHICKEN_COOP")
    )),

    //special
    BARN(createMap(
            new Building(1, 0, 0, 0, new CoinPrice(0), null, null, new BarnTile(), 0, TileSize.SIZE_3X3, "BARN")
    )),

    HELICOPTER(createMap(
            new Building(3, 0, 0, 0, new CoinPrice(0), null, null, StaticWorldTile.Type.ROAD.getTile(), 0, TileSize.SIZE_3X3, "HELICOPTER")
    )),

    TRAIN(createMap(
            new Building(5, 0, 0, 0, new CoinPrice(0), null, null, new TrainTile(), 0, TileSize.SIZE_1X3, "TRAIN")
    )),

    TOWN_HALL(createMap(new Building(6, 0, 0, 0, new CoinPrice(0), null, null, StaticWorldTile.Type.WATER.getTile(), 0, TileSize.SIZE_2X2, "TOWN_HALL")));
    private final Map<Integer, Building> buildings;

    @Nullable
    public Building getNextBuilding(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        PurchasedBuildings.Wrapper amountPurchased = user.getPurchasedBuildings().getNextBuilding(this);
        if (amountPurchased.slot() == -1) return null;

        if (amountPurchased.slot() == -2) {
            return buildings.get(user.getPurchasedBuildings().amountPurchased(this));
        }
        if (amountPurchased.placed()) {
            Building building = buildings.get(amountPurchased.slot());
            building.setNeedToBePlaced(true);
            return building;
        } else {
            if (amountPurchased.slot() == 0) {
                return buildings.get(0);
            }
            return buildings.get(amountPurchased.slot() - 1);
        }
    }

    private static Map<Integer, Building> createMap(Building... buildings) {
        Map<Integer, Building> map = new HashMap<>();
        int i = 0;
        for (Building building : buildings) {
            building.setSlot(i);
            map.put(i++, building);
        }
        return map;
    }

}
