package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildMenuType;
import me.webhead1104.township.data.enums.BuildingType;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class BuildManager {
    public void openMenu(Player player) {
        player.getInventory().clear();
        Inventory inventory = Bukkit.createInventory(null, 18, Msg.format("Build Menu"));
        inventory.setItem(2, MenuItems.buildMenuHousing);
        inventory.setItem(3, MenuItems.buildMenuCommunity);
        inventory.setItem(4, MenuItems.buildMenuFactories);
        inventory.setItem(5, MenuItems.buildMenuFarming);
        inventory.setItem(6, MenuItems.buildMenuSpecial);
        getCoinsAndCash(player, inventory);
        Utils.openInventory(player, inventory, uuid -> Bukkit.getScheduler().runTask(Township.getInstance(), () -> Township.getWorldManager().openWorldMenu(player)), null);
    }

    public void openMenu(Player player, BuildMenuType type) {
        openMenu(player, type, 0);
    }

    public void openMenu(Player player, BuildMenuType type, int page) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 18, type.getMenuTitle());
        inventory.setItem(9, MenuItems.backButton);
        getCoinsAndCash(player, inventory);
        inventory.setItem(17, MenuItems.homeButton);
        Map<Integer, List<Building>> map = new HashMap<>();
        for (BuildingType buildingType : type.getClazz().open(player, page)) {
            map.put(buildingType.ordinal(), new ArrayList<>());
            for (Building value : buildingType.getBuildings().values()) {
                map.get(buildingType.ordinal()).add(value);
            }
        }
        if (map.size() >= 7) {
            ItemBuilder next = new ItemBuilder(MenuItems.arrow).displayName(Msg.format("<white>Next Page")).id("build_arrow").pdcSetInt(Keys.newPageKey, page + 1);
            inventory.setItem(0, next.build());
            ItemBuilder previous = new ItemBuilder(MenuItems.arrow).displayName(Msg.format("<white>Previous Page")).id("build_arrow").pdcSetInt(Keys.newPageKey, page - 1);
            inventory.setItem(8, previous.build());
        }
        AtomicInteger slot = new AtomicInteger(1);
        map.forEach((buildingType, buildings) -> {
            int buildingSlot;
            if (buildings.size() > 1) {
                buildingSlot = user.getPurchasedBuildings().amountPurchased(BuildingType.values()[buildingType]) + 1;
            } else {
                buildingSlot = 0;
            }
            ItemBuilder builder = new ItemBuilder(buildings.get(buildingSlot).getItemStack(type.name(), player));
            builder.lore(Msg.format(String.format("<green>%s/%s purchased", user.getPurchasedBuildings().amountPurchased(BuildingType.values()[buildingType]), buildings.size())), 1);
            inventory.setItem(slot.getAndIncrement(), builder.build());
        });
        Utils.openInventory(player, inventory, uuid -> Township.getBuildManager().openMenu(Objects.requireNonNull(Bukkit.getPlayer(uuid))), null);
    }

    public void purchase(Player player, BuildingType buildingType, Inventory inventory, int slot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int buildingSlot;
        if (buildingType.getBuildings().size() > 1) {
            buildingSlot = user.getPurchasedBuildings().amountPurchased(buildingType) + 1;
        } else {
            buildingSlot = 0;
        }
        if (user.getPurchasedBuildings().isPurchased(buildingType, buildingSlot)) {
            Component component;
            if (buildingSlot > 0) {
                component = Msg.format("<white>You have already purchased this building!");
            } else {
                component = Msg.format("<white>You have already purchased all of this building!");
            }
            popupItem(inventory, slot, Msg.format("<red>Already Purchased!"), List.of(component));
            return;
        }
        Building building = buildingType.getBuildings().get(buildingSlot);
        if (building.getLevelNeeded() > user.getLevel().getLevel()) {
            popupItem(inventory, slot, Msg.format("<red>Level not high enough!"), List.of(
                    Msg.format("<white>You need to be level: <blue>%s</blue> or higher", building.getLevelNeeded()),
                    Msg.format("<white>to start the action")
            ));
            return;
        }
        if (building.getPopulationIncrease() > 0 && building.getMaxPopulationIncrease() == 0) {
            if (user.getPopulation() + building.getPopulationIncrease() > user.getMaxPopulation()) {
                popupItem(inventory, slot, Msg.format("<red>Population Cap Reached!"), List.of(
                        Msg.format("<white>You need to increase the"),
                        Msg.format("<white>population cap by <red>%s", user.getPopulation() + building.getPopulationIncrease()),
                        Msg.format("<white>to start the action")
                ));
                return;
            }
        }
        if (building.getPrice().has(player)) {
            popupItem(inventory, slot, Msg.format("<red>Not Enough Coins!"), List.of(building.getPrice().notEnoughComponent(player).color(NamedTextColor.WHITE)));
            return;
        }
        building.getPrice().take(player);
        placeMenu(player, buildingType, building, buildingSlot);
    }

    public void placeMenu(Player player, BuildingType buildingType, Building building, int buildingSlot) {
        placeMenu(player, buildingType, building, buildingSlot, 0, 27);
    }

    public void placeMenu(Player player, BuildingType buildingType, Building building, int buildingSlot, int slot, int section) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Inventory inventory = Township.getWorldManager().getWorld(player, section);
        if (new HashSet<>(building.getSize().toList(slot)).containsAll(MenuUtils.borderSlots(inventory))) {
            return;
        }
        player.getInventory().clear();
        ItemStack arrowUp = new ItemBuilder(MenuItems.arrow)
                .material(Material.ARROW)
                .id("build_world_arrow")
                .displayName(Msg.format("<dark_green>Click to scroll up!"))
                .pdcSetInt(Keys.newPageKey, section - 8)
                .pdcSetString(Keys.typeKey, buildingType.name().toUpperCase())
                .pdcSetInt(Keys.slot, buildingSlot)
                .build();
        ItemStack arrowRight = new ItemBuilder(MenuItems.arrow)
                .material(Material.ARROW)
                .id("build_world_arrow")
                .displayName(Msg.format("<dark_green>Click to scroll right!"))
                .pdcSetInt(Keys.newPageKey, section + 1)
                .pdcSetString(Keys.typeKey, buildingType.name().toUpperCase())
                .pdcSetInt(Keys.slot, buildingSlot)
                .build();
        ItemStack arrowDown = new ItemBuilder(MenuItems.arrow)
                .material(Material.ARROW)
                .id("build_world_arrow")
                .displayName(Msg.format("<dark_green>Click to scroll up!"))
                .pdcSetInt(Keys.newPageKey, section + 8)
                .pdcSetString(Keys.typeKey, buildingType.name().toUpperCase())
                .pdcSetInt(Keys.slot, buildingSlot)
                .build();
        ItemStack arrowLeft = new ItemBuilder(MenuItems.arrow)
                .material(Material.ARROW)
                .id("build_world_arrow")
                .displayName(Msg.format("<dark_green>Click to scroll left!"))
                .pdcSetInt(Keys.newPageKey, section - 1)
                .pdcSetString(Keys.typeKey, buildingType.name().toUpperCase())
                .pdcSetInt(Keys.slot, buildingSlot)
                .build();
        if (Township.getWorldManager().canShowArrowUp(section)) {
            player.getInventory().setItem(13, arrowUp);
        }
        if (Township.getWorldManager().canShowArrowLeft(section)) {
            player.getInventory().setItem(21, arrowLeft);
        }
        if (Township.getWorldManager().canShowArrowRight(section)) {
            player.getInventory().setItem(23, arrowRight);
        }
        if (Township.getWorldManager().canShowArrowDown(section)) {
            player.getInventory().setItem(31, arrowDown);
        }
        WorldSection worldSection = user.getWorld().getSection(section);
        int inventorySlot = 0;
        for (ItemStack content : inventory.getContents()) {
            ItemBuilder builder = new ItemBuilder(content);
            builder.id("build_place");
            builder.pdcSetInt(Keys.section, section);
            builder.pdcSetString(Keys.typeKey, buildingType.name().toUpperCase());
            builder.pdcSetInt(Keys.slot, buildingSlot);
            inventory.setItem(inventorySlot++, builder.build());
        }
        boolean canPlace = true;
        for (Integer i : building.getSize().toList(slot)) {
            if (!worldSection.getSlot(i).getTileType().equals(WorldTileType.GRASS)) {
                canPlace = false;
            }
            ItemBuilder builder = ItemBuilder.loading().id("build_place");
            builder.pdcSetString(Keys.typeKey, buildingType.name().toUpperCase());
            builder.pdcSetInt(Keys.slot, buildingSlot);
            builder.pdcSetInt(Keys.section, section);
            if (canPlace) {
                builder.material(Material.LIME_CONCRETE);
                builder.displayName(Msg.format("<green>You can place %s here!", Utils.thing2(building.getID())));
            } else {
                builder.material(Material.RED_CONCRETE);
                builder.displayName(Msg.format("<red>Unable to place building here!"));
                builder.lore(List.of(
                        Msg.format("<red>You cannot place a %s here!", Utils.thing2(building.getID())),
                        Msg.format("<red>%s is in the way!", Utils.thing2(worldSection.getSlot(i).getTileType().name()))
                ));
            }
            inventory.setItem(i, builder.build());
        }
        ItemBuilder confirmBuilder;
        if (canPlace) {
            confirmBuilder = new ItemBuilder(Material.LIME_CONCRETE, "build_confirm");
            confirmBuilder.displayName(Msg.format("<green>Click to place %s here!", Utils.thing2(building.getID())));
            confirmBuilder.pdcSetString(Keys.typeKey, buildingType.name().toUpperCase());
            confirmBuilder.pdcSetInt(Keys.slot, buildingSlot);
            confirmBuilder.pdcSetInt(Keys.section, section);
        } else {
            for (Integer i : building.getSize().toList(slot)) {
                ItemBuilder builder = ItemBuilder.loading().id("build_place");
                if (worldSection.getSlot(i).getTileType().equals(WorldTileType.GRASS)) {
                    builder.material(Material.YELLOW_CONCRETE);
                } else {
                    builder.material(Material.RED_CONCRETE);
                }
                builder.pdcSetString(Keys.typeKey, buildingType.name().toUpperCase());
                builder.pdcSetInt(Keys.slot, buildingSlot);
                builder.pdcSetInt(Keys.section, section);
                builder.displayName(Msg.format("<red>Unable to place building here!"));
                builder.lore(List.of(
                        Msg.format("<red>You cannot place a %s here!", Utils.thing2(building.getID())),
                        Msg.format("<red>%s is in the way!", Utils.thing2(worldSection.getSlot(i).getTileType().name()))
                ));
                inventory.setItem(i, builder.build());
            }
            confirmBuilder = new ItemBuilder(Material.RED_CONCRETE, "no_click");
            confirmBuilder.displayName(Msg.format("<red>Unable to place %s!", Utils.thing2(building.getID())));
        }
        player.getInventory().setItem(22, confirmBuilder.build());
        Utils.openInventory(player, inventory, uuid -> Township.getWorldManager().openWorldMenu(Objects.requireNonNull(Bukkit.getPlayer(uuid))), null);
    }

    public void confirm(Player player, BuildingType buildingType, Building building, int buildingSlot, int slot, int section) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        WorldSection worldSection = user.getWorld().getSection(section);
        for (Integer i : building.getSize().toList(slot)) {
            worldSection.getSlot(i).setTileType(WorldTileType.valueOf(buildingType.name().toUpperCase()));
        }
        user.getPurchasedBuildings().setPurchased(buildingType, buildingSlot, true);
        user.getPurchasedBuildings().recalculatePopulation(player);
        Township.getWorldManager().openWorldMenu(player);
    }

    private void popupItem(Inventory inventory, int slot, Component name, List<Component> lore) {
        ItemStack stack = Objects.requireNonNull(inventory.getItem(slot));
        ItemBuilder builder = new ItemBuilder(stack);
        builder.material(Material.RED_CONCRETE);
        builder.displayName(name);
        builder.lore(lore);
        builder.id("popup_item");
        inventory.setItem(slot, builder.build());
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> inventory.setItem(slot, stack), 20);
    }

    private void getCoinsAndCash(Player player, Inventory inventory) {
        ItemBuilder coins = new ItemBuilder(MenuItems.buildMenuCoins);
        coins.material(Material.GOLD_BLOCK);
        coins.displayName(Msg.format("<gold>Coins<white>: " + Township.getUserManager().getUser(player.getUniqueId()).getCoins()));
        inventory.setItem(12, coins.build());
        ItemBuilder cash = new ItemBuilder(MenuItems.buildMenuCash);
        cash.material(Material.LIME_CONCRETE);
        cash.displayName(Msg.format("<green>Cash<white>: " + Township.getUserManager().getUser(player.getUniqueId()).getCash()));
        inventory.setItem(14, cash.build());
    }
}
