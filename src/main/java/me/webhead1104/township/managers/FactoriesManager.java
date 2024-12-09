package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
import me.webhead1104.township.data.objects.Factories;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class FactoriesManager {

    //"12-14 completed, 27 being worked on,36-44 recipes"
    public void openFactoryMenu(Player player, FactoryType type) {
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (!(user.getLevel().getLevel() >= type.getLevelNeeded())) return;
        Factories factory = user.getFactories();
        Inventory inventory = Bukkit.createInventory(null, 54, type.getMenuTitle());
        //recipes
        AtomicInteger recipes = new AtomicInteger(45);
        for (RecipeType recipe : type.getRecipes()) {
            ItemBuilder builder = new ItemBuilder(recipe.getMenuItem());
            builder.pdcSetString(Keys.recipeTypeKey, recipe.name());
            builder.pdcSetString(Keys.factoryTypeKey, type.name());
            inventory.setItem(recipes.get(), builder.build());
            recipes.getAndIncrement();
        }
        AtomicInteger num2 = new AtomicInteger(12);
        //completed
        for (int i = 0; i < 3; ++i) {
            String id = factory.getCompleted(type, i).getID();
            if (!id.equals("none")) {
                ItemType item = ItemType.valueOf(id.toUpperCase());
                ItemBuilder builder = new ItemBuilder(MenuItems.completed);
                builder.material(item.getItemStack().getType());
                builder.displayName(item.getItemStack().getItemMeta().displayName());
                builder.lore(Msg.format("<green>Click to claim!"));
                builder.pdcSetString(Keys.recipeTypeKey, item.name());
                builder.pdcSetString(Keys.factoryTypeKey, type.getID());
                builder.pdcSetInt(Keys.slot, i);
                inventory.setItem(num2.get(), builder.build());
                num2.getAndIncrement();
            }
        }
        //working on
        RecipeType workingOn = factory.getWorkingOn(type);
        ItemBuilder builder = new ItemBuilder(MenuItems.workingOn);
        if (workingOn.equals(RecipeType.NONE)) {
            builder.displayName(Msg.format("<red>Nothing is being made right now, <aqua>Maybe you should made something!"));
            builder.material(Material.RED_CANDLE);
        } else {
            builder.displayName(workingOn.getItemType().getItemStack().displayName());
            workingOn.getItemType().getItemStack().getType();
        }
        builder.pdcSetString(Keys.factoryTypeKey, type.getID());
        inventory.setItem(36, builder.build());
        inventory.setItem(53, MenuItems.backButton);
        player.openInventory(inventory);
    }

    public void complete(Player player, int completedSlot, FactoryType factoryType, RecipeType recipeType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getFactories().setCompleted(factoryType, completedSlot, ItemType.NONE);
        user.getBarn().addAmountToItem(recipeType.getItemType(), 1);
        user.getLevel().addXp(recipeType.getXpGiven());
        openFactoryMenu(player, factoryType);
    }

    public void recipe(Player player, RecipeType recipeType, FactoryType factoryType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Factories factories = user.getFactories();
        AtomicBoolean good = new AtomicBoolean(true);
        recipeType.getRecipeItems().forEach((key, value) -> {
            if (!(user.getBarn().getItem(key) >= value)) good.set(false);
        });
        if (good.get()) {
            recipeType.getRecipeItems().forEach((key, value) -> user.getBarn().removeAmountFromItem(key, value));
            factories.addCompleted(factoryType, recipeType.getItemType());
            Township.getUserManager().setUser(player.getUniqueId(), user);
            openFactoryMenu(player, factoryType);
        }
    }
}
