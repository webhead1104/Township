package me.webhead1104.towncraft.listeners;

import me.devnatan.inventoryframework.*;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.events.TowncraftInventoryCloseEvent;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryViewImpl;
import me.webhead1104.towncraft.menus.ClickType;
import me.webhead1104.towncraft.menus.TowncraftSlotType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public record IFListener(ViewFrame viewFrame) implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent bukkitEvent) {
        if (!(bukkitEvent.getWhoClicked() instanceof Player player)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl(player));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getActiveContext();
        final Component clickedComponent = context.getComponentsAt(bukkitEvent.getRawSlot()).stream()
                .filter(Component::isVisible)
                .findFirst()
                .orElse(null);
        final ViewContainer clickedContainer = bukkitEvent.getClickedInventory() instanceof PlayerInventory
                ? viewer.getSelfContainer()
                : context.getContainer();

        final RootView root = context.getRoot();
        TowncraftInventoryClickEvent event = new TowncraftInventoryClickEvent(
                new TowncraftInventoryViewImpl(bukkitEvent.getView()),
                TowncraftSlotType.valueOf(bukkitEvent.getSlotType().name()),
                bukkitEvent.getRawSlot(),
                ClickType.valueOf(bukkitEvent.getClick().name())
        );
        final IFSlotClickContext clickContext = root.getElementFactory()
                .createSlotClickContext(bukkitEvent.getRawSlot(), viewer, clickedContainer, clickedComponent, event, false);

        root.getPipeline().execute(StandardPipelinePhases.CLICK, clickContext);

        bukkitEvent.setCancelled(event.isCancelled());
        bukkitEvent.setResult(Event.Result.valueOf(event.getResult().name()));
        if (event.getCurrentItem() == null) {
            bukkitEvent.setCurrentItem(null);
            return;
        }
        bukkitEvent.setCurrentItem((ItemStack) event.getCurrentItem().toPlatform());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClose(final InventoryCloseEvent bukkitEvent) {
        if (!(bukkitEvent.getPlayer() instanceof Player player)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl(player));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getCurrentContext();
        final RootView root = context.getRoot();
        TowncraftInventoryCloseEvent event = new TowncraftInventoryCloseEvent(
                new TowncraftInventoryViewImpl(bukkitEvent.getView()),
                TowncraftInventoryCloseEvent.Reason.valueOf(bukkitEvent.getReason().toString())
        );
        final IFCloseContext closeContext = root.getElementFactory().createCloseContext(viewer, context, event);

        root.getPipeline().execute(StandardPipelinePhases.CLOSE, closeContext);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl(event.getPlayer()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_PICKUP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_PICKUP));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl(event.getPlayer()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DROP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DROP));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((Player) event.getWhoClicked()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DRAG)) return;

        final boolean configValue = context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DRAG);
        final int size = event.getInventory().getSize();
        for (final int rawSlot : event.getRawSlots()) {
            if (!(rawSlot < size)) continue;

            event.setCancelled(configValue);
            break;
        }
    }
}
