package me.webhead1104.towncraft.features.world.build;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import org.jetbrains.annotations.NotNull;

public class BuildMenu extends TowncraftView {
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
        for (BuildMenuType.BuildMenu buildMenu : Towncraft.getDataLoader(BuildMenuType.class).values()) {
            context.slot(i++).onRender(slotRenderContext -> {
                TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
                itemStack.setName(buildMenu.getMenuTitle());
                itemStack.overrideNameColor();
                slotRenderContext.setItem(itemStack);
            }).onClick(slotClickContext -> {
                slotClickContext.openForPlayer(BuildMenuSelectBuildingMenu.class, buildMenu.key());
                openBackMenu.set(false, slotClickContext);
            });
        }
    }
}
