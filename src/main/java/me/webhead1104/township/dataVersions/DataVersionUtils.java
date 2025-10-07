package me.webhead1104.township.dataVersions;

import lombok.experimental.UtilityClass;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.serializers.TileSerializer;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.tiles.Tile;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DataVersionUtils {

    public static ConfigurationTransformation replaceBuilding(Tile tile) {
        return (rootNode) -> {
            ConfigurationNode worldMapNode = rootNode.node("world", "world-map");
            worldMapNode.childrenMap().forEach((sectionKey, sectionNode) -> {
                ConfigurationNode slotMapNode = sectionNode.node("slot-map");
                slotMapNode.childrenMap().forEach((slotKey, slotNode) -> {

                    try {
                        if (tile.equals(slotNode.get(Tile.class))) {
                            new TileSerializer().serialize(Tile.class, StaticWorldTile.Type.GRASS.getTile(), slotNode);
                        }
                    } catch (SerializationException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        };
    }

    public static ConfigurationTransformation setPurchasedBuilding(PurchasedBuildings.PurchasedBuilding purchasedBuilding, Key key, int buildingSlot) {
        return (rootNode) -> {
            try {
                ConfigurationNode purchasedBuildingsNode = rootNode.node("purchased-buildings", "purchased-buildings");
                List<PurchasedBuildings.PurchasedBuilding> purchasedBuildings = purchasedBuildingsNode.node(key.value()).getList(PurchasedBuildings.PurchasedBuilding.class, new ArrayList<>());
                while (purchasedBuildings.size() <= buildingSlot) {
                    purchasedBuildings.add(null);
                }
                purchasedBuildings.set(buildingSlot, purchasedBuilding);
                purchasedBuildingsNode.node(key.value()).setList(PurchasedBuildings.PurchasedBuilding.class, purchasedBuildings);
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
