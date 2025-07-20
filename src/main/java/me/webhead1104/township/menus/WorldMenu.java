package me.webhead1104.township.menus;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldMenu extends View {
    private final MutableState<Integer> sectionState = initialState();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.modifyConfig().title("World Menu");
        Player player = context.getPlayer();
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Township.getWorldManager().openConfirmCloseMenu(context.getPlayer());
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        world.getSection(sectionState.get(context)).getSlotMap().forEach((key, value) -> {
            ItemBuilder builder = new ItemBuilder(value.getTileType().getItem());
            if (value.getPlot() != null) {
                Plot plot = value.getPlot();
                builder.pdcSetString(Keys.plotDataKey, plot.toString());
                builder.id(plot.getPlotType().getId());
                builder.material(plot.getPlotType().getMenuItem().getType());
                builder.displayName(new ItemBuilder(plot.getPlotType().getMenuItem()).getDisplayName());
            }
            if (value.getExpansion() != null) {
                Expansion expansion = value.getExpansion();
                builder.pdcSetString(Keys.expansionDataKey, expansion.toString());
                builder.id("expansion");
                builder.material(Material.PODZOL);
                builder.displayName(Msg.format("Expansion"));
                builder.lore(List.of(Msg.format("<aqua>Click to open the expansion menu!")));
            }
            if (value.getTileType().equals(WorldTileType.TRAIN) && !user.getTrains().isUnlocked()) {
                builder.lore(List.of(Msg.format("<red>You need to purchase the trains!")));
            }
            if (value.getTileType().getAnimalType() != null && !user.getAnimals().getAnimalBuilding(value.getTileType().getAnimalType()).isUnlocked()) {
                builder.lore(List.of(Msg.format("<red>You need to purchase the " + Utils.thing2(value.getTileType().getId()) + "!")));
            }
            context.slot(key, builder.build());
        });
    }
}
