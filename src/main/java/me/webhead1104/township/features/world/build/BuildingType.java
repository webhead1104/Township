package me.webhead1104.township.features.world.build;

import com.google.common.base.Stopwatch;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.annotations.DependsOn;
import me.webhead1104.township.data.TileSize;
import me.webhead1104.township.data.objects.ConstructionMaterials;
import me.webhead1104.township.data.objects.PurchasedBuildings;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.dataLoaders.DataLoader;
import me.webhead1104.township.features.animals.AnimalType;
import me.webhead1104.township.features.factories.FactoryType;
import me.webhead1104.township.price.NoopPrice;
import me.webhead1104.township.price.Price;
import me.webhead1104.township.tiles.BuildingTile;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.*;

@DependsOn({AnimalType.class, FactoryType.class})
public class BuildingType implements DataLoader.KeyBasedDataLoader<List<BuildingType.Building>> {
    private final Map<Key, List<Building>> values = new HashMap<>();

    @Nullable
    public static Building getNextBuilding(Player player, Key buildingType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        PurchasedBuildings.Wrapper amountPurchased = user.getPurchasedBuildings().getNextBuilding(buildingType);
        List<Building> buildings = Township.getDataLoader(BuildingType.class).get(buildingType);
        if (amountPurchased.slot() == -1) return null;

        if (amountPurchased.slot() == -2) {
            return buildings.get(user.getPurchasedBuildings().amountPurchased(buildingType));
        }
        if (amountPurchased.placed()) {
            Building building = buildings.get(amountPurchased.slot());
            building.needToBePlaced = true;
            return building;
        } else {
            if (amountPurchased.slot() == 0) {
                return buildings.getFirst();
            }
            return buildings.get(amountPurchased.slot() - 1);
        }
    }

    public List<Building> get(Key key) {
        if (!values.containsKey(key)) {
            throw new RuntimeException("Building does not exist! key: " + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    public Collection<List<Building>> values() {
        return values.values();
    }

    @Override
    public void load() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<ConfigurationNode> nodes = getNodesFromMultipleFiles("/data/buildingTypes");
        try {
            for (ConfigurationNode node : nodes) {
                if (!node.isList()) {
                    throw new RuntimeException("Node is not list!");
                }

                List<Building> buildings = node.getList(Building.class);
                if (buildings == null) {
                    throw new NullPointerException("List is empty!");
                }
                int i = 0;
                for (Building building : buildings) {
                    building.slot = i++;
                    if (building.getTile() instanceof BuildingTile buildingTile) {
                        buildingTile.setBuildingType(building.key());
                        buildingTile.setBuildingSlot(building.getSlot());
                    }
                }
                values.put(buildings.getFirst().key(), buildings);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading building types! Please report the following stacktrace to Webhead1104:", e);
        }
        Township.logger.info("Loaded {} buildings in {} ms!", values.size(), stopwatch.elapsed().toMillis());
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static final class Building implements Keyed {
        @Required
        @Getter(value = AccessLevel.NONE)
        @Setting("key")
        private Key key;
        @Required
        @Setting("level_needed")
        private int levelNeeded;
        @Setting("population_needed")
        private int populationNeeded;
        @Setting("population_increase")
        private int populationIncrease;
        @Setting("max_population_increase")
        private int maxPopulationIncrease;
        @Setting("price")
        private Price price;
        @Nullable
        @Setting("construction_materials")
        private ConstructionMaterials constructionMaterials;
        @Nullable
        @Setting("time")
        private Duration time;
        @Required
        @Setting("tile")
        private Tile tile;
        @Setting("xp")
        private int xp;
        @Required
        @Setting("size")
        private TileSize size;
        private transient int slot;
        private transient boolean needToBePlaced;
        private transient String name;

        @PostProcess
        private void postProcess() {
            name = Utils.thing2(key.value());
            if (price == null) {
                price = NoopPrice.INSTANCE;
            }
        }

        @Override
        public @NotNull Key key() {
            return key;
        }

        public ItemStack getItemStack(Player player) {
            ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(name));

            User user = Township.getUserManager().getUser(player.getUniqueId());

            List<Component> lore = new ArrayList<>();
            lore.add(Component.empty());
            lore.add(Msg.format("<green>%s/%s purchased", user.getPurchasedBuildings().amountPurchased(key), Township.getDataLoader(BuildingType.class).get(key).size()));
            if (populationIncrease == 0 && maxPopulationIncrease > 0) {
                lore.add(Component.empty());
                lore.add(Msg.format(String.format("<red>+%s Max Population", maxPopulationIncrease)));
            } else if (populationIncrease > 0 && maxPopulationIncrease == 0) {
                lore.add(Component.empty());
                lore.add(Msg.format(String.format("<red>+%s Population", populationIncrease)));
                if (user.getPopulation() + populationIncrease > user.getMaxPopulation()) {
                    lore.add(Msg.format("<red>Need <red>Max Population<white>: %d", (user.getPopulation() + populationIncrease) - user.getMaxPopulation()));
                }
            }
            if (needToBePlaced) {
                lore.add(Component.empty());
                lore.add(Msg.format("<white>Something happened when you tried to place this."));
                lore.add(Msg.format("<white>So you can try and place it again!"));
            } else {
                if (levelNeeded > 0) {
                    if (user.getLevel() >= levelNeeded) {
                        lore.add(Component.empty());
                        lore.add(Msg.format("<blue>Level needed<white>: <green>%s/%s", user.getLevel(), levelNeeded));
                    } else {
                        lore.add(Component.empty());
                        lore.add(Msg.format("<blue>Level needed<white>: <red>%s/%s", user.getLevel(), levelNeeded));
                    }
                }
                if (populationNeeded > 0) {
                    lore.add(Component.empty());
                    if (user.getPopulation() >= populationNeeded) {
                        lore.add(Msg.format("<red>Population needed<white>: <green>%s/%s", user.getPopulation(), populationNeeded));
                    } else {
                        lore.add(Msg.format("<red>Population needed<white>: <red>%s/%s", user.getPopulation(), populationNeeded));
                    }
                }
            }
            lore.add(Component.empty());
            lore.add(price.getComponent(player));

            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
            return itemStack;
        }
    }
}
