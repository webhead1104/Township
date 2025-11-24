package me.webhead1104.towncraft.menus.pipeline;

import me.devnatan.inventoryframework.VirtualView;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.pipeline.PipelineContext;
import me.devnatan.inventoryframework.pipeline.PipelineInterceptor;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.CloseContext;

public class CancelledCloseInterceptor implements PipelineInterceptor<VirtualView> {

    @SuppressWarnings("ConstantValue")
    @Override
    public void intercept(PipelineContext<VirtualView> pipeline, VirtualView subject) {
        if (!(subject instanceof IFCloseContext)) return;

        final CloseContext context = (CloseContext) subject;
        if (!context.isCancelled()) return;

        final TowncraftPlayer player = context.getPlayer();
        final TowncraftItemStack cursor = player.getItemOnCursor();

        context.getRoot().nextTick(() -> context.getViewer().open(context.getContainer()));

        // suppress cursor null check since it can be null in legacy versions
        if ((cursor != null) && cursor.getMaterial() != TowncraftMaterial.AIR) player.setItemOnCursor(cursor);
    }
}
