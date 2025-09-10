package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.tiles.PlotTile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlotMenu extends View {
    private final MutableState<Plot> plot = initialState();
    private final MutableState<Boolean> openWorldMenu = mutableState(true);
    private final Map<Integer, PlotType> plotTypes = new HashMap<>();
    private PlotType selectedPlotType = PlotType.NONE;

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Plot Menu"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openWorldMenu.get(context)) {
                Township.getWorldManager().openWorldMenu(context.getPlayer());
            }
        }, 1);
        plotTypes.clear();
        selectedPlotType = PlotType.NONE;
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        Plot plot = this.plot.get(context);

        world.getSection(user.getSection()).getSlotMap().forEach((key, tile) -> {
            if (key != plot.getSlot()) {
                context.slot(key).onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
                    if (tile.onClick(clickContext)) {
                        openWorldMenu.set(false, clickContext);
                    }
                });
            }
        });
        context.slot(plot.getSlot()).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.GREEN_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click me with a ingredient to plant it!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            if (slotClickContext.getPlayer().getItemOnCursor().isEmpty() && selectedPlotType.equals(PlotType.NONE)) {
                return;
            }
            player.setItemOnCursor(ItemStack.empty());
            if (user.getCoins() >= selectedPlotType.getPrice()) {
                user.setCoins(user.getCoins() - selectedPlotType.getPrice());
                plot.setPlotType(selectedPlotType);
                plot.setInstant(Instant.now().plusSeconds(selectedPlotType.getTime().getSeconds()));
                plot.setClaimable(false);
                ((PlotTile) user.getWorld().getSection(plot.getSection()).getSlot(plot.getSlot())).setPlot(plot);
                slotClickContext.closeForPlayer();
            }
        });

        int i = 2;
        for (PlotType plotType : PlotType.values()) {
            if (plotType.equals(PlotType.NONE)) continue;
            if (plotType.getLevelNeeded() > user.getLevel()) continue;
            ItemStack itemStack = plotType.getMenuItem();
            String price;
            if (plotType.getPrice() == 0) {
                price = "<green>FREE!";
            } else {
                if (user.getCoins() >= plotType.getPrice()) {
                    price = "<green>%d/%d <gold>coins".formatted(plotType.getPrice(), user.getCoins());
                } else {
                    price = "<red>%d/%d <gold>coins".formatted(plotType.getPrice(), user.getCoins());
                }
            }
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Coins <white>needed: %s", price))));
            plotTypes.put(i + 81, plotType);
            player.getInventory().setItem(i++, itemStack);
        }

        ItemStack backButton = ItemStack.of(Material.BARRIER);
        backButton.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 81) {
            context.closeForPlayer();
            return;
        }
        if (plotTypes.containsKey(context.getSlot())) {
            PlotType plotType = plotTypes.get(context.getSlot());
            User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
            if (user.getCoins() >= plotType.getPrice()) {
                ItemStack itemStack = plotType.getMenuItem();
                context.getPlayer().setItemOnCursor(itemStack);
                selectedPlotType = plotType;
            }
        }
    }
}

