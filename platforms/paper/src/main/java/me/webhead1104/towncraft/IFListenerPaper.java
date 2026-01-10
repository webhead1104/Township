/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
package me.webhead1104.towncraft;

import me.devnatan.inventoryframework.*;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.pipeline.StandardPipelinePhases;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.events.TowncraftInventoryCloseEvent;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryViewPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

public record IFListenerPaper(ViewFrame viewFrame) implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent bukkitEvent) {
        if (!(bukkitEvent.getWhoClicked() instanceof Player p)) return;
        TowncraftPlayer player = new TowncraftPlayerPaperImpl(p);

        final Viewer viewer = viewFrame.getViewer(player);
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
        TowncraftInventory clickedInventory;
        if (bukkitEvent.getClickedInventory() instanceof PlayerInventory playerInventory) {
            clickedInventory = new TowncraftPlayerInventoryPaperImpl(playerInventory);
        } else {
            clickedInventory = new TowncraftInventoryPaperImpl(bukkitEvent.getClickedInventory());
        }
        TowncraftItemStack clickedItemStack = bukkitEvent.getCurrentItem() == null ? TowncraftItemStack.empty() :
                new TowncraftItemStackPaperImpl(bukkitEvent.getCurrentItem());
        TowncraftInventoryClickEvent event = new TowncraftInventoryClickEvent(
                new TowncraftInventoryViewPaperImpl(bukkitEvent.getView()),
                bukkitEvent.getRawSlot(),
                ClickType.valueOf(bukkitEvent.getClick().name()),
                bukkitEvent.getHotbarButton(),
                clickedItemStack,
                clickedInventory
        );
        final IFSlotClickContext clickContext = root.getElementFactory()
                .createSlotClickContext(bukkitEvent.getRawSlot(), viewer, clickedContainer, clickedComponent, event, false);

        root.getPipeline().execute(StandardPipelinePhases.CLICK, clickContext);

        bukkitEvent.setCancelled(event.isCancelled());
        bukkitEvent.setResult(Event.Result.valueOf(event.getResult().name()));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClose(final InventoryCloseEvent bukkitEvent) {
        if (!(bukkitEvent.getPlayer() instanceof Player player)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerPaperImpl(player));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getCurrentContext();
        final RootView root = context.getRoot();
        TowncraftInventoryCloseEvent event = new TowncraftInventoryCloseEvent(
                new TowncraftInventoryViewPaperImpl(bukkitEvent.getView()),
                TowncraftInventoryCloseEvent.Reason.valueOf(bukkitEvent.getReason().toString())
        );
        final IFCloseContext closeContext = root.getElementFactory().createCloseContext(viewer, context, event);

        root.getPipeline().execute(StandardPipelinePhases.CLOSE, closeContext);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent bukkitEvent) {
        final Viewer viewer = viewFrame().getViewer(new TowncraftPlayerPaperImpl(bukkitEvent.getPlayer()));
        if (viewer == null) return;

        final IFRenderContext context = viewer.getCurrentContext();
        final RootView root = context.getRoot();
        final IFCloseContext closeContext = root.getElementFactory().createCloseContext(viewer, context, bukkitEvent);

        root.getPipeline().execute(StandardPipelinePhases.CLOSE, closeContext);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerPaperImpl(player));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_PICKUP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_PICKUP));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerPaperImpl(event.getPlayer()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DROP)) return;

        event.setCancelled(context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DROP));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Viewer viewer = viewFrame.getViewer(new TowncraftPlayerPaperImpl((Player) event.getWhoClicked()));
        if (viewer == null) return;

        final IFContext context = viewer.getActiveContext();
        if (!context.getConfig().isOptionSet(ViewConfig.CANCEL_ON_DRAG)) return;

        final boolean configValue = context.getConfig().getOptionValue(ViewConfig.CANCEL_ON_DRAG);
        final int size = event.getInventory().getSize();
        for (final int slot : event.getInventorySlots()) {
            if (!(slot < size)) continue;

            event.setCancelled(configValue);
            break;
        }
    }
}
