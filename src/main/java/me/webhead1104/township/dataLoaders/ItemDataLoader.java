package me.webhead1104.township.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.Map;

public class ItemDataLoader implements DataLoader {
    @Getter
    private static final Map<Key, Item> map = new HashMap<>();

    public static Item get(Key key) {
        return map.get(key);
    }

    @Override
    public void load() {
//        try {
//            long start = System.currentTimeMillis();
//            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/levels.json"))))).build().load();
//            var nodeList = node.getList(Item.class);
//            if (nodeList == null || nodeList.isEmpty()) {
//                throw new RuntimeException("No items found!");
//            }
//            list.addAll(nodeList);
//            Township.logger.info("Loaded {} items in {} ms!", list.size(), System.currentTimeMillis() - start);
//        } catch (Exception e) {
//            throw new RuntimeException("An error occurred whilst loading items! Please report the following stacktrace to Webhead1104:", e);
//        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Item {
        @Setting("key")
        private Key key;
        @Setting("material")
        private Material material;
        @Setting("result")
        private Item result;
        @Setting("cash_given")
        private int cashGiven;
    }
}
