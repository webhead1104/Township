package me.webhead1104.towncraft.features.world.edit;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.data.objects.World;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.PlaceMenu;
import me.webhead1104.towncraft.features.world.WorldMenu;
import me.webhead1104.towncraft.features.world.WorldUtils;
import me.webhead1104.towncraft.features.world.build.BuildingType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.tiles.BuildingTile;
import me.webhead1104.towncraft.tiles.StaticWorldTile;
import me.webhead1104.towncraft.tiles.Tile;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WorldEditMenu extends TowncraftView {
    private final State<Integer> sectionState = initialState();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Edit Mode"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getUser().setSection(sectionState.get(context));
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        TowncraftPlayer player = context.getPlayer();
        User user = userState.get(context);
        World world = user.getWorld();
        WorldSection section = world.getSection(sectionState.get(context));

        section.getSlotMap().forEach((key, tile) -> context.slot(key).updateOnClick().onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext, section, key))).onClick(clickContext -> {
            if (handleTileClick(clickContext, tile)) {
                openBackMenu.set(false, clickContext);
            }
        }));

        WorldUtils.applyArrows(player, sectionState.get(context));

        TowncraftItemStack exitEdit = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
        exitEdit.setName(Msg.format("<red>Exit edit"));
        player.getInventory().setItem(8, exitEdit);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            if (context.getClickedSlot() == 68 && context.itemExists()) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 76 && context.itemExists()) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) + 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 66 && context.itemExists()) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 58 && context.itemExists()) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context) - 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 89 && context.itemExists()) {
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
        BuildingType.Building building = Towncraft.getDataLoader(BuildingType.class).get(key).get(buildingTile.getBuildingSlot());
        if (building == null) return false;

        int clickedSlot = context.getClickedSlot();
        User user = userState.get(context);
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
                    User u = ctx.getUser();
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
                    User u = cancelCtx.getUser();
                    for (Integer s : building.getSize().toList(anchor)) {
                        u.getWorld().getSection(startSection).setSlot(s, building.getTile());
                    }
                    Towncraft.getViewFrame().open(WorldMenu.class, cancelCtx.getPlayer(), startSection);
                }
        ));
        return true;
    }
}
