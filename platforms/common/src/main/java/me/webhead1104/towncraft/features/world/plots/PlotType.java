package me.webhead1104.towncraft.features.world.plots;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.price.Price;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PlotType implements DataLoader.KeyBasedDataLoader<PlotType.Plot> {
    private final Map<Key, Plot> values = new LinkedHashMap<>();

    @Override
    public Plot get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Plot type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    @Override
    public Collection<Plot> values() {
        return values.values();
    }

    @Override
    public void load() {
        try {
            List<Plot> list = getListFromFile("/data/plots.json", Plot.class);
            for (Plot plot : list) {
                plot.postProcess();
                values.put(plot.getKey(), plot);
            }
            Towncraft.getLogger().info("Loaded {} animals!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading plots! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @NoArgsConstructor
    @ConfigSerializable
    @AllArgsConstructor
    public static class Plot extends Keyed {
        @Required
        private Key key;
        @Required
        private Price price;
        @Required
        @Setting("level_needed")
        private int levelNeeded;
        @Required
        @Setting("xp_given")
        private int xpGiven;
        private Duration time;
        private transient ItemType.Item item;
        private transient TowncraftItemStack menuItem;

        @PostProcess
        private void postProcess() {
            this.item = Towncraft.getDataLoader(ItemType.class).get(this.key);
            Towncraft.getLogger().info("KEY = {}", key);
            if (key.equals(Towncraft.NONE_KEY)) {
                Towncraft.getLogger().info("IS NONE {}", this.key);
                TowncraftItemStack itemStack = TowncraftItemStackFactory.of(TowncraftMaterial.FARMLAND);
                itemStack.setName(Msg.format("Empty Plot"));
                itemStack.setLore(Msg.format("<green>You should plant something!"));
                menuItem = itemStack;
                return;
            }
            Towncraft.getLogger().info("IS NOT NONE {}", this.key);
            this.menuItem = this.item.getItemStack();
        }
    }
}
