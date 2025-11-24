package me.webhead1104.towncraft.menus.pipeline;

import me.devnatan.inventoryframework.VirtualView;
import me.devnatan.inventoryframework.pipeline.PipelineContext;
import me.devnatan.inventoryframework.pipeline.PipelineInterceptor;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import org.jetbrains.annotations.NotNull;

import static me.devnatan.inventoryframework.ViewConfig.CANCEL_ON_CLICK;

/**
 * Intercepted when a player clicks on the view container.
 * If the click is canceled, this interceptor ends the pipeline immediately.
 */
public final class GlobalClickInterceptor implements PipelineInterceptor<VirtualView> {

    @Override
    public void intercept(@NotNull PipelineContext<VirtualView> pipeline, @NotNull VirtualView subject) {
        if (!(subject instanceof SlotClickContext context)) return;

        final TowncraftInventoryClickEvent event = context.getClickOrigin();

        // inherit cancellation so we can un-cancel it
        context.setCancelled(event.isCancelled() || context.getConfig().isOptionSet(CANCEL_ON_CLICK, true));
        context.getRoot().onClick(context);
    }
}
