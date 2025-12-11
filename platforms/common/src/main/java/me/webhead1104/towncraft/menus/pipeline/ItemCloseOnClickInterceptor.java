package me.webhead1104.towncraft.menus.pipeline;

import me.devnatan.inventoryframework.VirtualView;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.component.ItemComponent;
import me.devnatan.inventoryframework.pipeline.PipelineContext;
import me.devnatan.inventoryframework.pipeline.PipelineInterceptor;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import org.jetbrains.annotations.NotNull;

/**
 * Intercepted when a player clicks on an item the view container. Checks if the container should be
 * closed when the item is clicked.
 */
public final class ItemCloseOnClickInterceptor implements PipelineInterceptor<VirtualView> {

    @Override
    public void intercept(@NotNull PipelineContext<VirtualView> pipeline, @NotNull VirtualView subject) {
        if (!(subject instanceof SlotClickContext context)) return;

        final TowncraftInventoryClickEvent event = context.getClickOrigin();
        if (event.isOutside()) return;

        final Component component = context.getComponent();
        if (!(component instanceof ItemComponent item) || !component.isVisible()) return;

        if (item.isCloseOnClick()) {
            context.closeForPlayer();
            pipeline.finish();
        }
    }
}
