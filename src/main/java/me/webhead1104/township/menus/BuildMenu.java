package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildMenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BuildMenu extends View {
    private final MutableState<Boolean> openWorldMenu = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(18);
        config.title("Build Menu");
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
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
        int i = 2;
        for (BuildMenuType buildMenuType : BuildMenuType.values()) {
            context.slot(i++).onRender(slotRenderContext -> {
                ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
                itemStack.setData(DataComponentTypes.ITEM_NAME, buildMenuType.getMenuTitle());
                itemStack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
                slotRenderContext.setItem(itemStack);
            }).onClick(slotClickContext -> {
                slotClickContext.openForPlayer(BuildMenuSelectBuildingMenu.class, buildMenuType);
                openWorldMenu.set(false, slotClickContext);
            });
        }
    }
}
