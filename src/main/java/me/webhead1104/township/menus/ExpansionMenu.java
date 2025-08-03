package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.TileSize;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.tiles.tiles.StaticWorldTile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpansionMenu extends View {
    private final MutableState<Expansion> expansion = initialState();
    private final MutableState<Boolean> openWorldMenu = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Expansion Menu"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openWorldMenu.get(context)) {
                Township.getWorldManager().openWorldMenu(context.getPlayer());
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        Expansion expansion = this.expansion.get(context);

        world.getSection(user.getSection()).getSlotMap().forEach((key, tile) -> {
            if (!TileSize.SIZE_3X3.toList(expansion.getSlot()).contains(key)) {
                context.slot(key).onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
                    if (tile.onClick(clickContext)) {
                        openWorldMenu.set(false, clickContext);
                    }
                });
            }
        });
        for (Integer i : TileSize.SIZE_3X3.toList(expansion.getSlot())) {
            context.slot(i).onRender(slotRenderContext -> {
                ItemStack itemStack = ItemStack.of(Material.RED_CONCRETE);
                if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getPrice()) {
                    itemStack = itemStack.withType(Material.LIME_CONCRETE);
                }
                itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Expansion"));
                slotRenderContext.setItem(itemStack);
            });
        }

        ItemStack priceItem = ItemStack.of(Material.GOLD_BLOCK);
        String priceColor;
        if (user.getCoins() >= expansion.getPrice()) {
            priceColor = "<green>";
        } else {
            priceColor = "<red>";
        }
        priceItem.setData(DataComponentTypes.ITEM_NAME, Msg.format("<gold>Coins <white>needed: %s%d/%d", priceColor, user.getCoins(), expansion.getPrice()));
        player.getInventory().setItem(2, priceItem);

        ItemStack populationItem = ItemStack.of(Material.BLUE_CONCRETE);
        String populationColor;
        if (user.getPopulation() >= expansion.getPopulationNeeded()) {
            populationColor = "<green>";
        } else {
            populationColor = "<red>";
        }
        populationItem.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Population <white>needed: %s%d/%d", populationColor, user.getPopulation(), expansion.getPopulationNeeded()));
        player.getInventory().setItem(6, populationItem);

        ItemStack buyItem;
        if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getPrice()) {
            buyItem = ItemStack.of(Material.LIME_CONCRETE);
            buyItem.setData(DataComponentTypes.ITEM_NAME, Msg.format("<gold>Click to buy!"));
        } else {
            buyItem = ItemStack.of(Material.RED_CONCRETE);
            buyItem.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>:("));
        }
        buyItem.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Coins <white>needed: %s%d/%d", priceColor, user.getCoins(), expansion.getPrice()), Msg.format("<red>Population <white>needed: %s%d/%d", populationColor, user.getPopulation(), expansion.getPopulationNeeded()))));
        player.getInventory().setItem(4, buyItem);

        ItemStack backButton = ItemStack.of(Material.BARRIER);
        backButton.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 85 && context.getItem() != null && context.getItem().getType() == Material.LIME_CONCRETE) {
            User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
            if (user.getCoins() >= expansion.get(context).getPrice() && user.getPopulation() >= expansion.get(context).getPopulationNeeded()) {
                user.setCoins(user.getCoins() - expansion.get(context).getPrice());
                TileSize.SIZE_3X3.toList(expansion.get(context).getSlot()).forEach(slot -> user.getWorld().getSection(user.getSection()).setSlot(slot, StaticWorldTile.Type.GRASS.getTile()));
                context.closeForPlayer();
            }
        } else if (context.getSlot() == 81) {
            context.closeForPlayer();
        }
    }
}
