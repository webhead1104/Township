package me.webhead1104.towncraft.menus.pipeline;

import me.devnatan.inventoryframework.VirtualView;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.component.ItemComponent;
import me.devnatan.inventoryframework.pipeline.PipelineContext;
import me.devnatan.inventoryframework.pipeline.PipelineInterceptor;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import org.jetbrains.annotations.NotNull;

/**
 * Intercepted when a player clicks on an item the view container.
 */
public final class ItemClickInterceptor implements PipelineInterceptor<VirtualView> {

    @Override
    public void intercept(@NotNull PipelineContext<VirtualView> pipeline, @NotNull VirtualView subject) {
        if (!(subject instanceof SlotClickContext context)) return;
        if (context.isOutsideClick()) return;

        final Component component = context.getComponent();
        if (component == null) return;

        if (component instanceof ItemComponent item) {

            // inherit cancellation so we can un-cancel it
            context.setCancelled(item.isCancelOnClick());
        }
    }
}
