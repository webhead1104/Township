package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.Factories;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class FactoriesManager {

    //"12-14 completed, 27 being worked on,36-44 recipes"
    public void openFactoryMenu(Player player, FactoryType type) {
        try {
            player.getInventory().clear();
            player.setItemOnCursor(ItemStack.empty());
            User user = Township.getUserManager().getUser(player.getUniqueId());
            Factories factories = user.getFactories();
            if (!factories.getFactory(type).isUnlocked()) return;
            Inventory inventory = Bukkit.createInventory(null, 54, type.getMenuTitle());
            //recipes
            AtomicInteger recipeSlot = new AtomicInteger(45);
            for (RecipeType recipe : type.getRecipes()) {
                ItemBuilder builder = new ItemBuilder(recipe.getMenuItem());
                builder.lore(calculateLore(recipe, user.getBarn()));
                builder.pdcSetString(Keys.recipeTypeKey, recipe.name());
                builder.pdcSetString(Keys.factoryTypeKey, type.name());
                inventory.setItem(recipeSlot.getAndIncrement(), builder.build());
            }
            //waiting
            AtomicInteger waitingSlot = new AtomicInteger(29);
            for (int i = 0; i < 3; i++) {
                RecipeType waiting = factories.getFactory(type).getWaiting(i);
                ItemBuilder builder;
                if (waiting.equals(RecipeType.NONE)) {
                    builder = new ItemBuilder(Material.HOPPER, "waiting");
                    builder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
                } else {
                    builder = new ItemBuilder(MenuItems.waiting);
                    builder.displayName(new ItemBuilder(waiting.getMenuItem()).getDisplayName());
                    builder.material(waiting.getMenuItem().getType());
                    builder.pdcSetString(Keys.recipeTypeKey, waiting.name());
                    builder.pdcSetString(Keys.factoryTypeKey, type.name());
                }
                inventory.setItem(waitingSlot.getAndIncrement(), builder.build());
            }
            AtomicInteger completedSlot = new AtomicInteger(12);
            //completed
            for (int i = 0; i < 3; ++i) {
                ItemType item = factories.getFactory(type).getCompleted(i);
                ItemBuilder builder;
                if (item.equals(ItemType.NONE)) {
                    builder = new ItemBuilder(Material.CHEST, "completed_no_claim");
                    builder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
                } else {
                    builder = new ItemBuilder(MenuItems.completed);
                    builder.material(item.getItemStack().getType());
                    builder.displayName(item.getItemStack().getItemMeta().displayName());
                    builder.lore(Msg.format("<green>Click to claim!"));
                    builder.pdcSetString(Keys.recipeTypeKey, item.name());
                    builder.pdcSetString(Keys.factoryTypeKey, type.getID());
                    builder.pdcSetInt(Keys.slot, i);
                }
                inventory.setItem(completedSlot.getAndIncrement(), builder.build());
            }
            //working on
            RecipeType workingOn = factories.getFactory(type).getWorkingOn();
            ItemBuilder builder = new ItemBuilder(MenuItems.workingOn);
            if (workingOn.equals(RecipeType.NONE)) {
                builder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
                builder.material(Material.RED_CANDLE);
            } else {
                builder.displayName(workingOn.getItemType().getItemStack().getItemMeta().displayName());
                builder.material(workingOn.getItemType().getItemStack().getType());
            }
            builder.pdcSetString(Keys.factoryTypeKey, type.getID());
            inventory.setItem(27, builder.build());
            BukkitTask task = new BukkitRunnable() {
                public void run() {
                    Factories factories = Township.getUserManager().getUser(player.getUniqueId()).getFactories();
                    if (factories.getFactory(type).getInstant().equals(Instant.EPOCH)) return;
                    if (Instant.now().isAfter(factories.getFactory(type).getInstant().minusSeconds(1))) {
                        RecipeType workingOn = factories.getFactory(type).getWorkingOn();
                        int completed = factories.getFactory(type).addCompleted(workingOn.getItemType());
                        int completedSlot = 12 + completed;
                        ItemBuilder builder = new ItemBuilder(workingOn.getMenuItem());
                        builder.lore(List.of(Msg.format("<green>Click to claim!")));
                        builder.pdcSetString(Keys.recipeTypeKey, workingOn.name());
                        builder.pdcSetString(Keys.factoryTypeKey, type.getID());
                        builder.pdcSetInt(Keys.slot, completed);
                        builder.id("completed");
                        inventory.setItem(completedSlot, builder.build());
                        factories.getFactory(type).setInstant(Instant.EPOCH);
                        if (factories.getFactory(type).hasWaiting()) {
                            Township.logger.info("Working on next item");
                            int waitingFactorySlot = factories.getFactory(type).getFirstWaiting();
                            RecipeType waiting = factories.getFactory(type).getWaiting(waitingFactorySlot);
                            factories.getFactory(type).setInstant(Instant.now().plusSeconds(waiting.getTime()));
                            factories.getFactory(type).setWorkingOn(waiting);
                            factories.getFactory(type).setWaiting(waitingFactorySlot, RecipeType.NONE);
                            ItemBuilder workingOnBuilder = new ItemBuilder(MenuItems.workingOn);
                            workingOnBuilder.material(workingOn.getMenuItem().getType());
                            workingOnBuilder.displayName(workingOn.getMenuItem().getItemMeta().displayName());
                            workingOnBuilder.lore(List.of(Msg.format("<gold>Time: " + Utils.format(Instant.now(), factories.getFactory(type).getInstant()))));
                            inventory.setItem(27, workingOnBuilder.build());
                            ItemBuilder waitingBuilder = new ItemBuilder(Material.HOPPER, "waiting");
                            waitingBuilder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
                            inventory.setItem(getWaitingSlotFromFactorySlot(waitingFactorySlot), waitingBuilder.build());
                        } else {
                            Township.logger.info("not Working on next item");
                            factories.getFactory(type).setWorkingOn(RecipeType.NONE);
                            ItemBuilder workingOnNothing = new ItemBuilder(MenuItems.workingOn);
                            workingOnNothing.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
                            workingOnNothing.material(Material.RED_CANDLE);
                            inventory.setItem(27, workingOnNothing.build());
                        }
                    } else {
                        String string = "<gold>Time: " + Utils.format(Instant.now(), factories.getFactory(type).getInstant());
                        Objects.requireNonNull(inventory.getItem(27)).editMeta(ItemMeta.class, meta -> meta.lore(List.of(Msg.format(string))));
                    }
                }
            }.runTaskTimer(Township.getInstance(), 0, 20);
            Utils.openInventory(player, inventory, uuid -> Township.getWorldManager().openWorldMenu(player), task);
        } catch (Exception e) {
            Township.logger.error("error", e);
        }
    }

    public void collectItem(Player player, int completedSlot, FactoryType factoryType, RecipeType recipeType, Inventory inventory, int clickedSlot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getFactories().getFactory(factoryType).setCompleted(completedSlot, ItemType.NONE);
        user.getBarn().addAmountToItem(recipeType.getItemType(), 1);
        user.getLevel().addXp(recipeType.getXpGiven());
        ItemBuilder builder = new ItemBuilder(Material.CHEST, "completed_no_claim");
        builder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should make something!"));
        inventory.setItem(clickedSlot, builder.build());
    }

    public void recipe(Player player, RecipeType recipeType, FactoryType factoryType, Inventory inventory, int clickedSlot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Factories factories = user.getFactories();
        if (factories.getFactory(factoryType).canStartWorking()) {
            AtomicBoolean good = new AtomicBoolean(true);
            recipeType.getRecipeItems().forEach((key, value) -> {
                if (!(user.getBarn().getItem(key) >= value)) good.set(false);
            });
            if (good.get()) {
                recipeType.getRecipeItems().forEach((key, value) -> user.getBarn().removeAmountFromItem(key, value));
                if (factories.getFactory(factoryType).getWorkingOn().equals(RecipeType.NONE)) {
                    Township.logger.info("setting working on");
                    factories.getFactory(factoryType).setWorkingOn(recipeType);
                    factories.getFactory(factoryType).setInstant(Instant.now().plusSeconds(recipeType.getTime()));
                    //working on
                    ItemBuilder workingOn = new ItemBuilder(MenuItems.workingOn);
                    workingOn.displayName(Msg.format("<white>" + Utils.thing2(recipeType.name().toLowerCase())));
                    workingOn.material(recipeType.getMenuItem().getType());
                    String string = "<gold>Time: " + Utils.format(Instant.now(), factories.getFactory(factoryType).getInstant());
                    workingOn.lore(List.of(Msg.format(string)));
                    inventory.setItem(27, workingOn.build());
                } else {
                    if (factories.getFactory(factoryType).canAddWaiting()) {
                        Township.logger.info("adding waiting");
                        int waitingFactorySlot = factories.getFactory(factoryType).addWaiting(recipeType);
                        ItemBuilder waiting = new ItemBuilder(MenuItems.waiting);
                        waiting.displayName(Msg.format("<white>" + Utils.thing2(recipeType.name().toLowerCase())));
                        waiting.material(recipeType.getMenuItem().getType());
                        inventory.setItem(getWaitingSlotFromFactorySlot(waitingFactorySlot), waiting.build());
                    }
                }
                //recipe
                ItemBuilder recipe = new ItemBuilder(inventory.getItem(clickedSlot));
                recipe.lore(calculateLore(recipeType, user.getBarn()));
                inventory.setItem(clickedSlot, recipe.build());
            }
        }
    }

    private List<Component> calculateLore(RecipeType recipeType, Barn barn) {
        List<Component> lore = new ArrayList<>();
        recipeType.getRecipeItems().forEach((key, value) -> {
            if (barn.getItem(key) >= value) {
                lore.add(Msg.format("<white>" + Utils.thing2(key.getID()) + ": <green>" + barn.getItem(key) + "/" + value));
            } else {
                lore.add(Msg.format("<white>" + Utils.thing2(key.getID()) + ": <red>" + barn.getItem(key) + "/" + value));
            }
        });
        return lore;
    }

    private int getWaitingSlotFromFactorySlot(int waitingFactorySlot) {
        return switch (waitingFactorySlot) {
            case 0 -> 29;
            case 1 -> 30;
            case 2 -> 31;
            default -> throw new IllegalStateException("Unexpected value: " + waitingFactorySlot);
        };
    }
}
