package net.cytonic.towncraft.listeners;

import me.devnatan.inventoryframework.*;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.events.TowncraftInventoryCloseEvent;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryViewImpl;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.menus.ClickType;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;

import java.util.prefs.Preferences;

public class IFListener {
    private final ViewFrame viewFrame = TowncraftPlatformManager.getViewFrame();

    @Listener
    public void onInventoryClick(InventoryPreClickEvent minestomEvent) {
        Towncraft.getLogger().info("Slot = {}", minestomEvent.getSlot());
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((CytosisPlayer) minestomEvent.getPlayer()));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getActiveContext();
        final Component clickedComponent = context.getComponentsAt(minestomEvent.getSlot()).stream()
                .filter(Component::isVisible)
                .findFirst()
                .orElse(null);
        final ViewContainer clickedContainer = minestomEvent.getInventory() instanceof PlayerInventory
                ? viewer.getSelfContainer()
                : context.getContainer();

        final RootView root = context.getRoot();
        TowncraftInventoryView view = new TowncraftInventoryViewImpl(
                (CytosisPlayer) minestomEvent.getPlayer(),
                minestomEvent.getPlayer().getOpenInventory(),
                minestomEvent.getPlayer().getInventory()
        );
        int hotbarSlot = -1;
        if (minestomEvent.getClick() instanceof Click.HotbarSwap(int slot, int ignored)) {
            hotbarSlot = slot;
        }
        int slot = getSlot(minestomEvent.getSlot(), view, !(minestomEvent.getInventory() instanceof PlayerInventory));
        TowncraftInventoryClickEvent event = new TowncraftInventoryClickEvent(
                view,
                minestomEvent.getSlot() == -999,
                slot,
                minestomEvent.getSlot(),
                getClickType(minestomEvent.getClick()),
                hotbarSlot
        );
        final IFSlotClickContext clickContext = root.getElementFactory()
                .createSlotClickContext(slot, viewer, clickedContainer, clickedComponent, event, false);

        root.getPipeline().execute(StandardPipelinePhases.CLICK, clickContext);

        minestomEvent.setCancelled(event.isCancelled());
    }

    private int getSlot(int rawSlot, TowncraftInventoryView view, boolean isTopInventory) {
        int numInTop = view.getTopInventory().getSize();

        if (isTopInventory) {
            Preferences.userRoot()
            return rawSlot; // 0 to 53 for chest
        }

// Clicking in player inventory
// rawSlot is 0-35 from Minestom

// Window layout:
// 54-80: Main inventory (3 rows, 27 slots)
// 81-89: Hotbar (1 row, 9 slots)

// Minestom player inventory:
// 0-8: Hotbar
// 9-35: Main inventory

        if (rawSlot >= 0 && rawSlot < 9) {
            // Hotbar (0-8) -> window slots 81-89
            return numInTop + 27 + rawSlot;
        } else if (rawSlot >= 9 && rawSlot < 36) {
            // Main inventory (9-35) -> window slots 54-80
            return numInTop + (rawSlot - 9);
        }

        return rawSlot;
    }

    private ClickType getClickType(Click click) {
        return switch (click) {
            case Click.Left ignored -> ClickType.LEFT;
            case Click.Right ignored -> ClickType.RIGHT;
            case Click.Middle ignored -> ClickType.MIDDLE;
            case Click.LeftShift ignored -> ClickType.SHIFT_LEFT;
            case Click.RightShift ignored -> ClickType.SHIFT_RIGHT;
            case Click.Double ignored -> ClickType.DOUBLE_CLICK;
            case Click.DropSlot ignored -> ClickType.DROP;
            case Click.HotbarSwap ignored -> ClickType.NUMBER_KEY;
            default -> throw new IllegalStateException("Unexpected value: " + click);
        };
    }

    @Listener
    public void onInventoryClose(final InventoryCloseEvent minestomEvent) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((CytosisPlayer) minestomEvent.getPlayer()));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getCurrentContext();
        final RootView root = context.getRoot();
        TowncraftInventoryCloseEvent event = getTowncraftInventoryCloseEvent(minestomEvent);
        final IFCloseContext closeContext = root.getElementFactory().createCloseContext(viewer, context, event);

        root.getPipeline().execute(StandardPipelinePhases.CLOSE, closeContext);
    }

    private TowncraftInventoryCloseEvent getTowncraftInventoryCloseEvent(InventoryCloseEvent minestomEvent) {
        TowncraftInventoryView view = new TowncraftInventoryViewImpl(
                (CytosisPlayer) minestomEvent.getPlayer(),
                minestomEvent.getInventory(),
                minestomEvent.getPlayer().getInventory()
        );
        TowncraftInventoryCloseEvent.Reason reason = minestomEvent.isFromClient() ? TowncraftInventoryCloseEvent.Reason.PLAYER : TowncraftInventoryCloseEvent.Reason.UNKNOWN;
        return new TowncraftInventoryCloseEvent(view, reason);
    }

    @Listener
    public void onItemPickup(PickupItemEvent event) {
        if (!(event.getEntity() instanceof CytosisPlayer player)) return;
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl(player));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_PICKUP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_PICKUP));
    }

    @Listener
    public void onItemDrop(ItemDropEvent event) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((CytosisPlayer) event.getPlayer()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DROP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DROP));
    }

    @Listener
    public void onInventoryDrag(InventoryPreClickEvent event) {
        if (!(event.getClick() instanceof Click.Drag drag)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((CytosisPlayer) event.getPlayer()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DRAG)) return;

        final boolean configValue = context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DRAG);
        final int size = event.getInventory().getSize();
        for (final int rawSlot : drag.slots()) {
            if (!(rawSlot < size)) continue;

            event.setCancelled(configValue);
            break;
        }
    }
}
