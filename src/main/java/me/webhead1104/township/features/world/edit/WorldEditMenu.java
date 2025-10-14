package me.webhead1104.township.features.world.edit;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.features.world.PlaceMenu;
import me.webhead1104.township.features.world.WorldMenu;
import me.webhead1104.township.features.world.WorldUtils;
import me.webhead1104.township.features.world.build.BuildingType;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.tiles.BuildingTile;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WorldEditMenu extends TownshipView {
    private final State<Integer> sectionState = initialState();

    public WorldEditMenu() {
        super(WorldMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Edit Mode"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        Township.getUserManager().getUser(context.getPlayer().getUniqueId()).setSection(sectionState.get(context));
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        WorldSection section = world.getSection(sectionState.get(context));

        section.getSlotMap().forEach((key, tile) -> context.slot(key).updateOnClick().onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
            if (handleTileClick(clickContext, tile)) {
                openBackMenu.set(false, clickContext);
            }
        }));

        WorldUtils.applyArrows(player, sectionState.get(context));

        ItemStack exitEdit = ItemStack.of(Material.BARRIER);
        exitEdit.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Exit Edit"));
        player.getInventory().setItem(8, exitEdit);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            if (context.getClickedSlot() == 68 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 76 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 66 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 58 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 89 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context));
                openBackMenu.set(false, context);
            }
        }
    }

    private boolean handleTileClick(SlotClickContext context, Tile tile) {
        if (!(tile instanceof BuildingTile buildingTile)) {
            return false;
        }
        if (buildingTile.isImmovable()) return false;

        Key key = buildingTile.getBuildingType();
        if (key == null) return false;
        BuildingType.Building building = Township.getDataLoader(BuildingType.class).get(key).get(buildingTile.getBuildingSlot());
        if (building == null) return false;

        int clickedSlot = context.getClickedSlot();
        User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
        WorldSection section = user.getWorld().getSection(sectionState.get(context));

        int anchor = WorldUtils.findAnchor(clickedSlot, building.getSize(), section, buildingTile);
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
                    u.getPurchasedBuildings().getPurchasedBuilding(key, building.getSlot()).ifPresent(pb -> {
                        pb.setPlaced(true);
                        pb.setSection(newSection);
                    });
                    u.recalculatePopulation();
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
