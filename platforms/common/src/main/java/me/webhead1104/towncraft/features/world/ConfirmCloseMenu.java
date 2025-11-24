package me.webhead1104.towncraft.features.world;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.utils.Msg;
import org.jetbrains.annotations.NotNull;

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
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            itemStack.setName(Msg.format("<red>Are you sure you want to close towncraft?"));
            itemStack.setLore(
                    Msg.format("<red>If so click this item!"),
                    Msg.format("<green>Or if don't want to close towncraft hit the Esc key or click the back button!")
            );
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            userState.get(slotClickContext).save();
            context.closeForEveryone();
            openBackMenu.set(false, slotClickContext);
            TowncraftPlayer player = slotClickContext.getPlayer();
            //todo
//            if (Towncraft.getInventoryManager().getPlayerInventory(player.getUniqueId()).isPresent()) {
//                Towncraft.getInventoryManager().returnItemsToPlayer(player);
//            }
        });

        context.slot(8).onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
            itemStack.setName(Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);
    }
}
