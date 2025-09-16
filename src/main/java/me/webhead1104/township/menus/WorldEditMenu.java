package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildingType;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.tiles.BuildingTile;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WorldEditMenu extends View {
    private final State<Integer> sectionState = initialState();
    private final MutableState<Boolean> openConfirmClose = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Edit Mode"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
        Township.getUserManager().getUser(context.getPlayer().getUniqueId()).setSection(sectionState.get(context));
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openConfirmClose.get(context)) {
                Township.getWorldManager().openWorldMenu(context.getPlayer(), sectionState.get(context));
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        WorldSection section = world.getSection(sectionState.get(context));

        section.getSlotMap().forEach((key, tile) -> context.slot(key).updateOnClick().onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
            if (handleTileClick(clickContext, tile)) {
                openConfirmClose.set(false, clickContext);
            }
        }));

        Township.getWorldManager().applyArrows(player, sectionState.get(context));

        ItemStack exitEdit = ItemStack.of(Material.BARRIER);
        exitEdit.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Exit Edit"));
        player.getInventory().setItem(8, exitEdit);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            if (context.getClickedSlot() == 68 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 1);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 76 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 8);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 66 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 1);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 58 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 8);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 89 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context));
                openConfirmClose.set(false, context);
            }
        }
    }

    private boolean handleTileClick(SlotClickContext context, Tile tile) {
        if (!(tile instanceof BuildingTile buildingTile)) {
            return false;
        }

        BuildingType buildingType = buildingTile.getBuildingType();
        if (buildingType == null) return false;
        Building building = buildingType.getBuildings().get(buildingTile.getBuildingSlot());
        if (building == null) return false;

        int clickedSlot = context.getClickedSlot();
        User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
        WorldSection section = user.getWorld().getSection(sectionState.get(context));

        int anchor = Township.getWorldManager().findAnchor(clickedSlot, building.getSize(), section, buildingTile);
        if (anchor < 0) return false;

        for (Integer s : building.getSize().toList(anchor)) {
            section.setSlot(s, StaticWorldTile.Type.GRASS.getTile());
        }

        int startSection = sectionState.get(context);
        context.openForPlayer(PlaceMenu.class, Map.of(
                "TILE_SIZE", building.getSize(),
                "START_SECTION", startSection,
                "START_ANCHOR", anchor,
                "TITLE", Msg.format("Edit"),
                "ON_PLACE", (PlaceMenu.PlaceAction) (ctx, newSection, newAnchor) -> {
                    User u = Township.getUserManager().getUser(ctx.getPlayer().getUniqueId());
                    for (Integer s : building.getSize().toList(newAnchor)) {
                        u.getWorld().getSection(newSection).setSlot(s, building.getTile());
                    }
                    u.getPurchasedBuildings().getPurchasedBuilding(buildingType, building.getSlot()).ifPresent(pb -> {
                        pb.setPlaced(true);
                        pb.setSection(newSection);
                    });
                    u.getPurchasedBuildings().recalculatePopulation(ctx.getPlayer());
                    ctx.openForPlayer(WorldMenu.class, newSection);
                },
                "ON_CANCEL", (PlaceMenu.CancelAction) cancelCtx -> {
                    User u = Township.getUserManager().getUser(cancelCtx.getPlayer().getUniqueId());
                    for (Integer s : building.getSize().toList(anchor)) {
                        u.getWorld().getSection(startSection).setSlot(s, building.getTile());
                    }
                    Township.getViewFrame().open(WorldMenu.class, cancelCtx.getPlayer(), startSection);
                }
        ));
        return true;
    }
}
