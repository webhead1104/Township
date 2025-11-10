package me.webhead1104.towncraft.features.world;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfirmCloseMenu extends TowncraftView {

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelInteractions();
        config.title(Msg.format("<red>Are you sure?"));
        config.size(1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        context.slot(4).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.RED_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Are you sure you want to close towncraft?"));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<red>If so click this item!"),
                    Msg.format("<green>Or if don't want to close towncraft hit the Esc key or click the back button!"))));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            userState.get(slotClickContext).save();
            context.closeForEveryone();
            openBackMenu.set(false, slotClickContext);
            Player player = slotClickContext.getPlayer();
            if (Towncraft.getInventoryManager().getPlayerInventory(player.getUniqueId()).isPresent()) {
                Towncraft.getInventoryManager().returnItemsToPlayer(player);
            }
        });

        context.slot(8).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.BARRIER);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);
    }
}
