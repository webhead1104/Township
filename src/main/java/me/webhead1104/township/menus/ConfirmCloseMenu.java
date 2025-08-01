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
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfirmCloseMenu extends View {
    private final MutableState<Boolean> openWorld = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelInteractions();
        config.title(Msg.format("<red>Are you sure?"));
        config.size(1);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openWorld.get(context)) {
                Township.getViewFrame().open(WorldMenu.class, context.getPlayer(), Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getSection());
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        context.slot(4).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.RED_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Are you sure you want to close township?"));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<red>If so click this item!"),
                    Msg.format("<green>Or if don't want to close township hit the Esc key or click the back button!"))));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            Player player = slotClickContext.getPlayer();
            Township.getDatabase().setData(Township.getUserManager().getUser(player.getUniqueId()));
            context.closeForEveryone();
            openWorld.set(false, slotClickContext);
            if (Township.getInventoryManager().getPlayerInventory(player.getUniqueId()).isPresent()) {
                Township.getInventoryManager().returnItemsToPlayer(player);
            }
        });

        context.slot(8).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.BARRIER);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);
    }
}
