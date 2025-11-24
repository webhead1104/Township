package me.devnatan.inventoryframework;

import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.pipeline.Pipeline;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.menus.component.TowncraftItemComponentBuilder;
import me.webhead1104.towncraft.menus.context.CloseContext;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.pipeline.CancelledCloseInterceptor;
import me.webhead1104.towncraft.menus.pipeline.GlobalClickInterceptor;
import me.webhead1104.towncraft.menus.pipeline.ItemClickInterceptor;
import me.webhead1104.towncraft.menus.pipeline.ItemCloseOnClickInterceptor;
import org.jetbrains.annotations.ApiStatus;

/**
 * Bukkit platform {@link PlatformView} implementation.
 */
@ApiStatus.OverrideOnly
public class View
        extends PlatformView<
        ViewFrame,
        TowncraftPlayer,
        TowncraftItemComponentBuilder,
        Context,
        OpenContext,
        CloseContext,
        RenderContext,
        SlotClickContext> {

    @Override
    public void registerPlatformInterceptors() {
        final Pipeline<? super VirtualView> pipeline = getPipeline();
        pipeline.intercept(StandardPipelinePhases.CLICK, new ItemClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLICK, new GlobalClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLICK, new ItemCloseOnClickInterceptor());
        pipeline.intercept(StandardPipelinePhases.CLOSE, new CancelledCloseInterceptor());
    }

    @Override
    public final void nextTick(Runnable task) {
        Towncraft.runTaskNextTick(task);
    }
}
