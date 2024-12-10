package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class BarnManager {

    public final Map<Integer, BarnUpgrade> upgradeMap = new HashMap<>();
    private final Map<Integer, Map<Integer, ItemType>> barnPageMap = new HashMap<>();
    private final Map<UUID, Integer> barnPages = new HashMap<>();

    public void openMenu(Player player, int page) {
        player.getInventory().clear();
        if (!barnPages.containsKey(player.getUniqueId())) barnPages.put(player.getUniqueId(), 1);
        barnPages.put(player.getUniqueId(), page);
        Inventory inventory = Bukkit.createInventory(null, 54, Msg.format("The barn"));
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Barn barn = user.getBarn();
        AtomicInteger i = new AtomicInteger(0);
        for (Map.Entry<Integer, ItemType> entry : barnPageMap.get(page).entrySet()) {
            if (user.getBarn().getItem(entry.getValue()) == 0) continue;
            ItemType type = entry.getValue();
            ItemBuilder builder = new ItemBuilder(type.getItemStack().getType(), type.getID() + "_barn");
            builder.displayName(Msg.format("You have <aqua>" + barn.getItem(type) + "</aqua> of <yellow>" + Utils.thing2(type.getID()) + "</yellow>"));
            builder.lore(List.of());
            builder.setRarity(ItemRarity.COMMON);
            inventory.setItem(i.getAndIncrement(), builder.build());
        }
        if (barnPageMap.containsKey(page + 1)) {
            ItemBuilder up = new ItemBuilder(MenuItems.arrow)
                    .id("barn_arrow")
                    .displayName(Msg.format("<dark_green>Click to scroll up!"))
                    .material(Material.ARROW)
                    .pdcSetInt(Keys.newPageKey, page + 1);
            player.getInventory().setItem(11, up.build());
        }
        if (page > 1) {
            ItemBuilder down = new ItemBuilder(MenuItems.arrow)
                    .id("barn_arrow")
                    .material(Material.ARROW)
                    .displayName(Msg.format("<dark_green>Click to scroll down!"))
                    .pdcSetInt(Keys.newPageKey, page - 1);
            player.getInventory().setItem(33, down.build());
        }
        ItemBuilder storage = new ItemBuilder(MenuItems.barnStorage);
        if (user.getBarn().getBarnUpgrade().getBarnStorage() >= user.getBarn().storage()) {
            storage.material(Material.LIME_CONCRETE);
            storage.displayName(Msg.format("<green>" + user.getBarn().storage() + "/" + user.getBarn().getBarnUpgrade().getBarnStorage()));
        } else {
            storage.material(Material.RED_CONCRETE);
            storage.displayName(Msg.format("<red>Full! " + user.getBarn().storage() + "/" + user.getBarn().getBarnUpgrade().getBarnStorage()));
        }
        List<Component> lore = new ArrayList<>();
        user.getBarn().getItemMap().forEach((key, value) -> {
            if (value != 0) {
                lore.add(Msg.format("<white>" + Utils.thing2(key.getID()) + ": " + value));
            }
        });
        storage.lore(lore);
        player.getInventory().setItem(0, storage.build());
        ItemBuilder upgrade = new ItemBuilder(MenuItems.barnUpgrade);
        Component line1;
        if (canUpgrade(player)) {
            upgrade.material(Material.LIME_CONCRETE);
            line1 = Msg.format("<green>Click to upgrade!");
        } else {
            upgrade.material(Material.RED_CONCRETE);
            line1 = Msg.format("<red>You need to get more materials to upgrade!");
        }
        Component hammers;
        if (user.getBarn().getItem(ItemType.HAMMER) >= user.getBarn().getBarnUpgrade().getToolsNeeded()) {
            hammers = Msg.format("<dark_green>Hammers: " + user.getBarn().getItem(ItemType.HAMMER) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        } else {
            hammers = Msg.format("<red>Hammers: " + user.getBarn().getItem(ItemType.HAMMER) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        }
        Component nails;
        if (user.getBarn().getItem(ItemType.NAIL) >= user.getBarn().getBarnUpgrade().getToolsNeeded()) {
            nails = Msg.format("<dark_green>Nails: " + user.getBarn().getItem(ItemType.NAIL) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        } else {
            nails = Msg.format("<red>Nails: " + user.getBarn().getItem(ItemType.NAIL) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        }
        Component paint;
        if (user.getBarn().getItem(ItemType.PAINT) >= user.getBarn().getBarnUpgrade().getToolsNeeded()) {
            paint = Msg.format("<dark_green>Paint buckets: " + user.getBarn().getItem(ItemType.PAINT) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        } else {
            paint = Msg.format("<red>Paint buckets: " + user.getBarn().getItem(ItemType.PAINT) + "/" + user.getBarn().getBarnUpgrade().getToolsNeeded());
        }
        upgrade.displayName(Msg.format("<white>Barn level: " + user.getBarn().getBarnUpgrade().getId()));
        upgrade.lore(List.of(line1, hammers, nails, paint));
        player.getInventory().setItem(8, upgrade.build());
        player.getInventory().setItem(7, MenuItems.backButton);
        player.openInventory(inventory);
    }

    public void openSellMenu(Player player, ItemType itemType, int amount) {
        openMenu(player, barnPages.get(player.getUniqueId()));
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(itemType) == 0) return;

        if (amount != 1) {
            ItemBuilder decrease = new ItemBuilder(MenuItems.barnDecreaseAmount);
            decrease.material(Material.RED_CANDLE);
            decrease.displayName(Msg.format("<red>Click to decrease the amount!"));
            decrease.lore(List.of(Msg.format("<blue>Currently at " + amount)));
            decrease.pdcSetInt(Keys.barnSellAmountKey, amount);
            decrease.pdcSetString(Keys.itemTypeKey, itemType.name());
            player.getInventory().setItem(3, decrease.build());
        }

        ItemBuilder sell = new ItemBuilder(MenuItems.barnSell);
        sell.material(Material.LIME_CONCRETE);
        String name = Utils.thing2(itemType.getID());
        sell.displayName(Msg.format("<green>Click to sell <aqua>" + amount + " <green>of <yellow>" + name + " <green>for <aqua>" + (itemType.getSellPrice() * amount) + " <gold>coins!"));
        sell.pdcSetInt(Keys.barnSellAmountKey, amount);
        sell.pdcSetString(Keys.itemTypeKey, itemType.name());
        player.getInventory().setItem(4, sell.build());

        if (user.getBarn().getItem(itemType) > amount) {
            ItemBuilder increase = new ItemBuilder(MenuItems.barnIncreaseAmount);
            increase.material(Material.GREEN_CANDLE);
            increase.displayName(Msg.format("<green>Click to increase the amount!"));
            increase.lore(List.of(Msg.format("<blue>Currently at " + amount)));
            increase.pdcSetInt(Keys.barnSellAmountKey, amount);
            increase.pdcSetString(Keys.itemTypeKey, itemType.name());
            player.getInventory().setItem(5, increase.build());
        }
    }

    public void increaseAmount(Player player, ItemType itemType, int newAmount) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(itemType) >= newAmount) {
            openSellMenu(player, itemType, newAmount);
        } else {
            openMenu(player, barnPages.get(player.getUniqueId()));
            ItemBuilder error = new ItemBuilder(Material.BARRIER, "barn_not_enough_items");
            error.displayName(Msg.format("<red>You don't have enough to sell this amount!"));
            player.getInventory().setItem(4, error.build());
        }
    }

    public void decreaseAmount(Player player, ItemType itemType, int newAmount) {
        if (newAmount == 0) {
            openMenu(player, barnPages.get(player.getUniqueId()));
            ItemBuilder error = new ItemBuilder(Material.BARRIER, "barn_not_enough_items");
            error.displayName(Msg.format("<red>You don't have enough to sell this amount!"));
            player.getInventory().setItem(4, error.build());
        } else {
            openSellMenu(player, itemType, newAmount);
        }
    }

    public void sellItem(Player player, ItemType itemType, int amount) {
        if (amount == 0) return;
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().removeAmountFromItem(itemType, amount);
        user.setCoins(user.getCoins() + itemType.getSellPrice() * amount);
        openMenu(player, barnPages.get(player.getUniqueId()));
    }

    public boolean canUpgrade(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int toolsNeeded = user.getBarn().getBarnUpgrade().getToolsNeeded();
        return user.getBarn().getItem(ItemType.HAMMER) >= toolsNeeded && user.getBarn().getItem(ItemType.NAIL) >= toolsNeeded && user.getBarn().getItem(ItemType.PAINT) >= toolsNeeded;
    }

    public void upgrade(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (canUpgrade(player)) {
            BarnUpgrade newUpgrade = upgradeMap.get(user.getBarn().getBarnUpgrade().getId() + 1);
            user.getBarn().setBarnUpgrade(newUpgrade);
            user.getBarn().removeAmountFromItem(ItemType.HAMMER, newUpgrade.getToolsNeeded());
            user.getBarn().removeAmountFromItem(ItemType.NAIL, newUpgrade.getToolsNeeded());
            user.getBarn().removeAmountFromItem(ItemType.PAINT, newUpgrade.getToolsNeeded());
            openMenu(player, barnPages.get(player.getUniqueId()));
        }
    }

    public void loadPages() {
        Township.logger.info("Loading barn");
        long start = System.currentTimeMillis();
        int page = 1;
        int slot = 0;
        int i = 0;
        for (ItemType type : ItemType.values()) {
            if (type.equals(ItemType.NONE)) continue;
            if (slot == 54) {
                barnPageMap.get(page).put(slot, type);
                slot = 0;
                page++;
            }
            if (i == 0) {
                barnPageMap.put(1, new HashMap<>());
                i++;
            }
            barnPageMap.get(page).put(slot, type);
            slot++;
        }
        Township.logger.info("Barn loaded in {} ms!", System.currentTimeMillis() - start);
    }

    public void loadUpgrades() {
        AtomicInteger storage = new AtomicInteger(70);
        AtomicInteger a = new AtomicInteger(20);
        for (int i = 1; i < 101; i++) {
            if (i == 1) {
                upgradeMap.put(i, new BarnUpgrade(i, i, 70));
                continue;
            }
            if (i == 3) {
                a.set(25);
            } else if (i == 39) {
                a.set(50);
            } else if (i == 59) {
                a.set(75);
            }
            int b = storage.get() + a.get();
            storage.set(b);
            upgradeMap.put(i, new BarnUpgrade(i, i, storage.get() + a.get()));
        }
    }
}
