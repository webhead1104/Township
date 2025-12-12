package me.webhead1104.towncraft.features.world.plots;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
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
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlotMenu extends TowncraftView {
    private final State<Integer> slotState = initialState("slot");
    private final State<Integer> sectionState = initialState("section");
    private final MutableState<Map<Integer, PlotType>> plotTypes = mutableState(new HashMap<>());
    private final MutableState<PlotType> selectedPlotTypeState = mutableState(PlotType.NONE);
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
            PlotType selectedPlotType = selectedPlotTypeState.get(slotClickContext);
            if (slotClickContext.getPlayer().getItemOnCursor().isEmpty() && selectedPlotType.equals(PlotType.NONE)) {
                return;
            }
            player.setItemOnCursor(TowncraftItemStack.empty());
            if (selectedPlotType.getPrice().has(user)) {
                selectedPlotType.getPrice().take(user);
                PlotTile plotTile = (PlotTile) worldSectionState.get(slotClickContext).getSlot(slotState.get(slotClickContext));
                plotTile.setPlotType(selectedPlotType);
                plotTile.setInstant(Instant.now().plus(selectedPlotType.getTime()));
                slotClickContext.closeForPlayer();
            }
        });

        int i = 2;
        for (PlotType plotType : PlotType.values()) {
            if (plotType.equals(PlotType.NONE)) continue;
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
        Map<Integer, PlotType> plotTypes = this.plotTypes.get(context);
        if (plotTypes.containsKey(context.getSlot())) {
            PlotType plotType = plotTypes.get(context.getSlot());
            if (plotType.getPrice().has(userState.get(context))) {
                TowncraftItemStack itemStack = plotType.getMenuItem();
                context.getPlayer().setItemOnCursor(itemStack);
                selectedPlotTypeState.set(plotType, context);
            }
        }
    }
}

