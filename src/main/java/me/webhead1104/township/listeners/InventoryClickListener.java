package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.*;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@NoArgsConstructor
public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            ItemBuilder builder = new ItemBuilder(item);
            if (builder.getMeta() != null && builder.pdcHas(ItemBuilder.townshipIdKey)) {
                event.setCancelled(true);
                String itemID = builder.pdcGetString(ItemBuilder.townshipIdKey);
                Player player = (Player) event.getWhoClicked();
                switch (itemID.toLowerCase()) {
                    case "cow_feed" -> Township.getAnimalsManager().feed(player, AnimalType.COWSHED);
                    case "milk" -> Township.getAnimalsManager().pickup(player, AnimalType.COWSHED, event.getSlot());
                    case "chicken_feed" -> Township.getAnimalsManager().feed(player, AnimalType.CHICKEN_COOP);
                    case "egg" -> Township.getAnimalsManager().pickup(player, AnimalType.CHICKEN_COOP, event.getSlot());
                    case "arrow_up" ->
                            Township.getWorldManager().openWorldMenu(player, Township.getUserManager().getUser(player.getUniqueId()).getSection() - 8);
                    case "arrow_left" ->
                            Township.getWorldManager().openWorldMenu(player, Township.getUserManager().getUser(player.getUniqueId()).getSection() - 1);
                    case "arrow_right" ->
                            Township.getWorldManager().openWorldMenu(player, Township.getUserManager().getUser(player.getUniqueId()).getSection() + 1);
                    case "arrow_down" ->
                            Township.getWorldManager().openWorldMenu(player, Township.getUserManager().getUser(player.getUniqueId()).getSection() + 8);
                    case "township", "back_button" -> Township.getWorldManager().openWorldMenu(player);
                    case "completed" -> {
                        FactoryType factoryType = FactoryType.valueOf(builder.pdcGetString(ItemBuilder.factoryTypeKey).toUpperCase());
                        RecipeType recipeType = RecipeType.valueOf(builder.pdcGetString(ItemBuilder.recipeTypeKey).toUpperCase());
                        int slot = builder.pdcGetInt(ItemBuilder.factoryCompletedSlotKey);
                        Township.getFactoriesManager().complete(player, slot, factoryType, recipeType);
                    }
                    case "bread_recipe", "cookie_recipe", "bagel_recipe", "cow_feed_recipe", "chicken_feed_recipe",
                         "cream_recipe", "cheese_recipe", "butter_recipe", "yogurt_recipe", "sugar_recipe",
                         "syrup_recipe", "caramel_recipe" -> {
                        FactoryType type = FactoryType.valueOf(builder.pdcGetString(ItemBuilder.factoryTypeKey).toUpperCase());
                        RecipeType recipeType = RecipeType.valueOf(builder.pdcGetString(ItemBuilder.recipeTypeKey).toUpperCase());
                        Township.getFactoriesManager().recipe(player, recipeType, type);
                    }
                    case "cowshed" -> Township.getAnimalsManager().openAnimalMenu(player, AnimalType.COWSHED);
                    case "chicken_coop" -> Township.getAnimalsManager().openAnimalMenu(player, AnimalType.CHICKEN_COOP);
                    case "bakery" -> Township.getFactoriesManager().openFactoryMenu(player, FactoryType.BAKERY);
                    case "feed_mill" -> Township.getFactoriesManager().openFactoryMenu(player, FactoryType.FEED_MILL);
                    case "dairy_factory" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.DAIRY_FACTORY);
                    case "sugar_factory" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.SUGAR_FACTORY);
                    case "expansion" -> {
                        Expansion expansion = Expansion.fromJson(builder.pdcGetString(ItemBuilder.expansionDataKey));
                        Township.getExpansionManager().openExpansionMenu(player, expansion);
                    }
                    case "expansion_buy" -> {
                        Expansion expansion = Expansion.fromJson(builder.pdcGetString(ItemBuilder.expansionDataKey));
                        Township.getExpansionManager().buyExpansion(player, expansion);
                    }
                    case "none_plot" -> {
                        if (event.getCursor().isEmpty()) {
                            Township.getPlotManager().openMenu(player);
                        } else Township.getPlotManager().plant(player, item, event.getCursor());
                    }
                    case "barn_arrow_up" -> {
                        int page = builder.pdcGetInt(ItemBuilder.barnArrowCurrentKey);
                        Township.getBarnManager().openMenu(player, page + 1);
                    }
                    case "barn_arrow_down" -> {
                        int page = builder.pdcGetInt(ItemBuilder.barnArrowCurrentKey);
                        Township.getBarnManager().openMenu(player, page - 1);
                    }
                    case "barn" -> Township.getBarnManager().openMenu(player, 1);
                    case "barn_sell" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(ItemBuilder.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(ItemBuilder.barnSellAmountKey);
                        Township.getBarnManager().sellItem(player, itemType, amount);
                    }
                    case "barn_increase" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(ItemBuilder.itemTypeKey).toUpperCase());
                        int newAmount = builder.pdcGetInt(ItemBuilder.barnSellAmountKey) + 1;
                        Township.getBarnManager().increaseAmount(player, itemType, newAmount);
                    }
                    case "barn_decrease" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(ItemBuilder.itemTypeKey).toUpperCase());
                        int newAmount = builder.pdcGetInt(ItemBuilder.barnSellAmountKey) - 1;
                        Township.getBarnManager().decreaseAmount(player, itemType, newAmount);
                    }
                    case "barn_upgrade" -> Township.getBarnManager().upgrade(player);
                    case "train" -> Township.getTrainManager().openMenu(player);
                    case "train_buy" ->
                            Township.getTrainManager().purchaseTrain(player, builder.pdcGetInt(ItemBuilder.trainKey));
                    case "train_collect" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(ItemBuilder.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(ItemBuilder.itemAmountKey);
                        int train = builder.pdcGetInt(ItemBuilder.trainKey);
                        int car = builder.pdcGetInt(ItemBuilder.trainCarKey);
                        Township.getTrainManager().collectItem(player, itemType, amount, train, car);
                    }
                    case "train_give" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(ItemBuilder.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(ItemBuilder.itemAmountKey);
                        int train = builder.pdcGetInt(ItemBuilder.trainKey);
                        int car = builder.pdcGetInt(ItemBuilder.trainCarKey);
                        Township.getTrainManager().giveItem(player, itemType, amount, train, car);
                    }
                }
                //plots
                for (PlotType plotType : PlotType.values()) {
                    if (plotType.equals(PlotType.NONE)) continue;
                    if (itemID.equals(plotType.getId().toLowerCase() + "_type_select")) {
                        Township.getPlotManager().selectCropType(plotType, player);
                    } else if (itemID.equals(plotType.getId().toLowerCase())) {
                        Township.getPlotManager().harvest(player, item);
                    }
                }
                for (ItemType itemType : ItemType.values()) {
                    if (itemType.equals(ItemType.NONE)) continue;
                    if (itemID.equals(itemType.getID().toLowerCase() + "_barn")) {
                        Township.getBarnManager().openSellMenu(player, itemType, 1);
                    }
                }
            }
        } else {
            if (event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) || event.getAction().equals(InventoryAction.DROP_ONE_CURSOR) || event.getAction().equals(InventoryAction.NOTHING)) {
                event.setCancelled(true);
            } else {
                ItemBuilder builder = new ItemBuilder(Objects.requireNonNull(event.getInventory()).getItem(event.getSlot()));
                if (builder.getMeta() != null && builder.pdcHas(ItemBuilder.townshipIdKey)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
