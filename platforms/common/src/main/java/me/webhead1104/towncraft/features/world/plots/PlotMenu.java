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
package me.webhead1104.towncraft.features.world.plots;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.data.objects.World;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.tiles.PlotTile;
import me.webhead1104.towncraft.tiles.Tile;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlotMenu extends TowncraftView {
    private final State<Integer> slotState = initialState("slot");
    private final State<Integer> sectionState = initialState("section");
    private final MutableState<Map<Integer, PlotType.Plot>> plotTypes = mutableState(new HashMap<>());
    private final MutableState<Key> selectedPlotTypeKeyState = mutableState(Towncraft.NONE_KEY);
    private final State<PlotType.Plot> selectedPlotTypeState = computedState(context -> Towncraft.getDataLoader(PlotType.class).get(selectedPlotTypeKeyState.get(context)));
    private final State<WorldSection> worldSectionState = computedState(context -> userState.get(context).getWorld().getSection(sectionState.get(context)));

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Plot Menu"));
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        TowncraftPlayer player = context.getPlayer();
        User user = userState.get(context);
        World world = user.getWorld();

        world.getSection(user.getSection()).getSlotMap().forEach((slot, mapTile) -> {
            if (!slot.equals(slotState.get(context))) {
                context.slot(slot).updateOnClick().onUpdate(slotContext -> {
                    WorldSection section = worldSectionState.get(slotContext);
                    Tile tile = section.getSlot(slot);
                    tile.onUpdate(slotContext, section, slot);
                }).onRender(slotRenderContext -> {
                    WorldSection section = worldSectionState.get(slotRenderContext);
                    Tile tile = section.getSlot(slot);
                    slotRenderContext.setItem(tile.render(slotRenderContext, section, slot));
                });
            }
        });
        context.slot(slotState.get(context)).onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.GREEN_CONCRETE);
            itemStack.setName(Msg.format("<green>Click me with a ingredient to plant it!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            PlotType.Plot selectedPlotType = selectedPlotTypeState.get(slotClickContext);
            if (slotClickContext.getPlayer().getItemOnCursor().isEmpty() && selectedPlotType.equals(Towncraft.NONE_KEY)) {
                return;
            }
            player.setItemOnCursor(TowncraftItemStack.empty());
            if (selectedPlotType.getPrice().has(user)) {
                selectedPlotType.getPrice().take(user);
                PlotTile plotTile = (PlotTile) worldSectionState.get(slotClickContext).getSlot(slotState.get(slotClickContext));
                plotTile.setPlotType(selectedPlotType.getKey());
                plotTile.setInstant(Instant.now().plus(selectedPlotType.getTime()));
                slotClickContext.closeForPlayer();
            }
        });

        int i = 2;
        for (PlotType.Plot plotType : Towncraft.getDataLoader(PlotType.class).values()) {
            if (plotType.equals(Towncraft.NONE_KEY)) continue;
            if (plotType.getLevelNeeded() > user.getLevel()) continue;
            TowncraftItemStack itemStack = plotType.getMenuItem();
            itemStack.setLore(plotType.getPrice().getComponent(user));
            plotTypes.get(context).put(i + 81, plotType);
            player.getInventory().setItem(i++, itemStack);
        }

        TowncraftItemStack backButton = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
        backButton.setName(Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 81) {
            context.closeForPlayer();
            return;
        }
        Map<Integer, PlotType.Plot> plotTypes = this.plotTypes.get(context);
        if (plotTypes.containsKey(context.getSlot())) {
            PlotType.Plot plotType = plotTypes.get(context.getSlot());
            if (plotType.getPrice().has(userState.get(context))) {
                TowncraftItemStack itemStack = plotType.getMenuItem();
                context.getPlayer().setItemOnCursor(itemStack);
                selectedPlotTypeKeyState.set(plotType.getKey(), context);
                context.update();
            }
        }
    }
}

