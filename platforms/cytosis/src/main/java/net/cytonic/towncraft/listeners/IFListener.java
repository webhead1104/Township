/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cytonic.towncraft.listeners;

import me.devnatan.inventoryframework.*;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.events.TowncraftInventoryCloseEvent;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryViewImpl;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackImpl;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.menus.ClickType;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;

public class IFListener {
    private final ViewFrame viewFrame = TowncraftPlatformManager.getViewFrame();

    @Listener
    public void onInventoryClick(InventoryPreClickEvent minestomEvent) {
        boolean isPlayerInventory = minestomEvent.getInventory() instanceof PlayerInventory;
        int size = 0;
        if (minestomEvent.getPlayer().getOpenInventory() != null) {
            size = minestomEvent.getPlayer().getOpenInventory().getSize();
        }

        int slot = getSlot(minestomEvent.getSlot(), size, !isPlayerInventory);
        TowncraftPlayer player = new TowncraftPlayerImpl((CytosisPlayer) minestomEvent.getPlayer());
        Viewer viewer = viewFrame.getViewer(player);
        if (viewer == null) return;

        if (minestomEvent.getClick() instanceof Click.DropSlot) {
            IFContext context = viewer.getActiveContext();
            if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DROP)) return;
            minestomEvent.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DROP));
            return;
        }
        if (minestomEvent.getClick() instanceof Click.LeftDrag || minestomEvent.getClick() instanceof Click.RightDrag) {
            IFContext context = viewer.getActiveContext();
            if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DRAG)) return;
            minestomEvent.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DRAG));
            return;
        }

        IFRenderContext context = viewer.getActiveContext();
        Component clickedComponent =
                context
                        .getComponentsAt(slot)
                        .stream()
                        .filter(Component::isVisible)
                        .findFirst()
                        .orElse(null);
        ViewContainer clickedContainer;
        if (minestomEvent.getInventory() instanceof PlayerInventory) {
            clickedContainer = viewer.getSelfContainer();
        } else {
            clickedContainer = context.getContainer();
        }

        RootView root = context.getRoot();
        TowncraftInventoryView view = new TowncraftInventoryViewImpl(
                (CytosisPlayer) minestomEvent.getPlayer(),
                minestomEvent.getPlayer().getOpenInventory(),
                minestomEvent.getPlayer().getInventory()
        );

        TowncraftInventory clickedInventory;
        if (minestomEvent.getInventory() instanceof PlayerInventory playerInventory) {
            clickedInventory = new TowncraftPlayerInventoryImpl(playerInventory);
        } else {
            clickedInventory = new TowncraftInventoryImpl(minestomEvent.getInventory());
        }
        TowncraftInventoryClickEvent towncraftEvent = new TowncraftInventoryClickEvent(
                view,
                slot,
                getClickType(minestomEvent.getClick()),
                (minestomEvent.getClick() instanceof Click.HotbarSwap swap) ? swap.hotbarSlot() : -999,
                new TowncraftItemStackImpl(minestomEvent.getClickedItem()),
                clickedInventory
        );
        IFSlotClickContext clickContext =
                root.getElementFactory().createSlotClickContext(
                        slot,
                        viewer,
                        clickedContainer,
                        clickedComponent,
                        towncraftEvent,
                        false
                );

        root.getPipeline().execute(StandardPipelinePhases.CLICK, clickContext);

        minestomEvent.setCancelled(towncraftEvent.isCancelled());
    }

    private int getSlot(int rawSlot, int sizeInTop, boolean isTopInventory) {
        if (isTopInventory) {
            return rawSlot;
        }
        if (rawSlot <= 8) {
            return rawSlot + sizeInTop + 27;
        }
        rawSlot -= 9;
        return rawSlot + sizeInTop;
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

    @Listener
    public void onQuit(final PlayerDisconnectEvent minestomEvent) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerImpl((CytosisPlayer) minestomEvent.getPlayer()));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getCurrentContext();
        final RootView root = context.getRoot();
        final IFCloseContext closeContext = root.getElementFactory().createCloseContext(viewer, context, minestomEvent);

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
