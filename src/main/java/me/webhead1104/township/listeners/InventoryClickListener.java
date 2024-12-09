package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.*;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
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
            if (builder.getMeta() != null && builder.pdcHas(Keys.townshipIdKey)) {
                event.setCancelled(true);
                String itemID = builder.pdcGetString(Keys.townshipIdKey);
                Player player = (Player) event.getWhoClicked();
                switch (itemID.toLowerCase()) {
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
                        FactoryType factoryType = FactoryType.valueOf(builder.pdcGetString(Keys.factoryTypeKey).toUpperCase());
                        RecipeType recipeType = RecipeType.valueOf(builder.pdcGetString(Keys.recipeTypeKey).toUpperCase());
                        int slot = builder.pdcGetInt(Keys.slot);
                        Township.getFactoriesManager().complete(player, slot, factoryType, recipeType);
                    }
                    //todo fix this mess
                    case "cowshed_1" -> Township.getAnimalsManager().openAnimalMenu(player, AnimalType.COWSHED_1);
                    case "cowshed_2" -> Township.getAnimalsManager().openAnimalMenu(player, AnimalType.COWSHED_2);
                    case "cowshed_3" -> Township.getAnimalsManager().openAnimalMenu(player, AnimalType.COWSHED_3);
                    case "chicken_coop_1" ->
                            Township.getAnimalsManager().openAnimalMenu(player, AnimalType.CHICKEN_COOP_1);
                    case "chicken_coop_2" ->
                            Township.getAnimalsManager().openAnimalMenu(player, AnimalType.CHICKEN_COOP_2);
                    case "chicken_coop_3" ->
                            Township.getAnimalsManager().openAnimalMenu(player, AnimalType.CHICKEN_COOP_3);
                    case "bakery" -> Township.getFactoriesManager().openFactoryMenu(player, FactoryType.BAKERY);
                    case "feed_mill_1" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.FEED_MILL_1);
                    case "feed_mill_2" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.FEED_MILL_2);
                    case "feed_mill_3" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.FEED_MILL_3);
                    case "dairy_factory" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.DAIRY_FACTORY);
                    case "sugar_factory" ->
                            Township.getFactoriesManager().openFactoryMenu(player, FactoryType.SUGAR_FACTORY);
                    case "expansion" -> {
                        Expansion expansion = Expansion.fromJson(builder.pdcGetString(Keys.expansionDataKey));
                        Township.getExpansionManager().openExpansionMenu(player, expansion);
                    }
                    case "expansion_buy" -> {
                        Expansion expansion = Expansion.fromJson(builder.pdcGetString(Keys.expansionDataKey));
                        Township.getExpansionManager().buyExpansion(player, expansion);
                    }
                    case "none_plot" -> {
                        if (event.getCursor().isEmpty()) {
                            Township.getPlotManager().openMenu(player);
                        } else Township.getPlotManager().plant(player, item, event.getCursor());
                    }
                    case "barn_arrow_up" -> {
                        int page = builder.pdcGetInt(Keys.barnArrowCurrentKey);
                        Township.getBarnManager().openMenu(player, page + 1);
                    }
                    case "barn_arrow_down" -> {
                        int page = builder.pdcGetInt(Keys.barnArrowCurrentKey);
                        Township.getBarnManager().openMenu(player, page - 1);
                    }
                    case "barn" -> Township.getBarnManager().openMenu(player, 1);
                    case "barn_sell" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(Keys.barnSellAmountKey);
                        Township.getBarnManager().sellItem(player, itemType, amount);
                    }
                    case "barn_increase" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int newAmount = builder.pdcGetInt(Keys.barnSellAmountKey) + 1;
                        Township.getBarnManager().increaseAmount(player, itemType, newAmount);
                    }
                    case "barn_decrease" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int newAmount = builder.pdcGetInt(Keys.barnSellAmountKey) - 1;
                        Township.getBarnManager().decreaseAmount(player, itemType, newAmount);
                    }
                    case "barn_upgrade" -> Township.getBarnManager().upgrade(player);
                    case "train" -> Township.getTrainManager().openMenu(player);
                    case "train_buy" ->
                            Township.getTrainManager().purchaseTrain(player, builder.pdcGetInt(Keys.trainKey));
                    case "train_collect" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(Keys.itemAmountKey);
                        int train = builder.pdcGetInt(Keys.trainKey);
                        int car = builder.pdcGetInt(Keys.trainCarKey);
                        Township.getTrainManager().collectItem(player, itemType, amount, train, car);
                    }
                    case "train_give" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(Keys.itemAmountKey);
                        int train = builder.pdcGetInt(Keys.trainKey);
                        int car = builder.pdcGetInt(Keys.trainCarKey);
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
                for (RecipeType recipeType : RecipeType.values()) {
                    if (recipeType.equals(RecipeType.NONE)) continue;
                    if (itemID.equals(recipeType.getId())) {
                        FactoryType factoryType = FactoryType.valueOf(builder.pdcGetString(Keys.factoryTypeKey).toUpperCase());
                        Township.getFactoriesManager().recipe(player, recipeType, factoryType);
                    }
                }
                for (AnimalType animalType : AnimalType.values()) {
                    if (itemID.equals(animalType.getProductType().getID())) {
                        int slot = builder.pdcGetInt(Keys.slot);
                        Township.getAnimalsManager().pickup(player, animalType, slot);
                    } else if (itemID.equals(animalType.getFeedType().getID())) {
                        Township.getAnimalsManager().feed(player, animalType);
                    }
                }
            }
        } else {
            if (event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) || event.getAction().equals(InventoryAction.DROP_ONE_CURSOR) || event.getAction().equals(InventoryAction.NOTHING)) {
                event.setCancelled(true);
            } else {
                ItemBuilder builder = new ItemBuilder(Objects.requireNonNull(event.getInventory()).getItem(event.getSlot()));
                if (builder.getMeta() != null && builder.pdcHas(Keys.townshipIdKey)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
