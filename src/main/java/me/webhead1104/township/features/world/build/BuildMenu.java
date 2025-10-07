package me.webhead1104.township.features.world.build;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.township.dataLoaders.BuildMenuType;
import me.webhead1104.township.features.world.WorldMenu;
import me.webhead1104.township.menus.TownshipView;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BuildMenu extends TownshipView {
    public BuildMenu() {
        super(WorldMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(18);
        config.title("Build Menu");
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        int i = 2;
        for (BuildMenuType.BuildMenu buildMenu : BuildMenuType.values()) {
            context.slot(i++).onRender(slotRenderContext -> {
                ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
                itemStack.setData(DataComponentTypes.ITEM_NAME, buildMenu.getMenuTitle());
                itemStack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
                slotRenderContext.setItem(itemStack);
            }).onClick(slotClickContext -> {
                slotClickContext.openForPlayer(BuildMenuSelectBuildingMenu.class, buildMenu.key());
                openBackMenu.set(false, slotClickContext);
            });
        }
    }
}
