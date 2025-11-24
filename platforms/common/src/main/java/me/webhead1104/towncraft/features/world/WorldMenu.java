package me.webhead1104.towncraft.features.world;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.dataLoaders.LevelDataLoader;
import me.webhead1104.towncraft.features.world.build.BuildMenu;
import me.webhead1104.towncraft.features.world.build.BuildingType;
import me.webhead1104.towncraft.features.world.edit.WorldEditMenu;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.tiles.BuildingTile;
import me.webhead1104.towncraft.tiles.StaticWorldTile;
import me.webhead1104.towncraft.tiles.Tile;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldMenu extends TowncraftView {
    private final State<Integer> sectionState = initialState();
    private final State<WorldSection> worldSectionState = computedState(context -> userState.get(context).getWorld().getSection(sectionState.get(context)));

    public WorldMenu() {
        super(ConfirmCloseMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("World Menu"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getUser().setSection(sectionState.get(context));
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        TowncraftPlayer player = context.getPlayer();
        User user = context.getUser();
        WorldSection worldSection = user.getWorld().getSection(sectionState.get(context));
        worldSection.getSlotMap().forEach((slot, mapTile) -> {
            mapTile.onLoad(context, worldSection, slot);
            context.slot(slot).updateOnClick().onUpdate(slotContext -> {
                WorldSection section = worldSectionState.get(slotContext);
                Tile tile = section.getSlot(slot);
                tile.onUpdate(slotContext, section, slot);
            }).onRender(slotRenderContext -> {
                WorldSection section = worldSectionState.get(slotRenderContext);
                Tile tile = section.getSlot(slot);
                slotRenderContext.setItem(tile.render(slotRenderContext, section, slot));
            }).onClick(clickContext -> {
                WorldSection section = worldSectionState.get(clickContext);
                Tile tile = section.getSlot(slot);
                if (clickContext.isShiftRightClick()) {
                    if (tile instanceof BuildingTile buildingTile) {
                        if (buildingTile.isImmovable()) return;
                        BuildingType.Building building = Towncraft.getDataLoader(BuildingType.class).get(buildingTile.getBuildingType()).get(buildingTile.getBuildingSlot());
                        int clickedSlot = clickContext.getClickedSlot();
                        int anchor = WorldUtils.findAnchor(clickedSlot, building.getSize(), worldSection, buildingTile);

                        if (anchor >= 0) {
                            for (Integer s : building.getSize().toList(anchor)) {
                                worldSection.setSlot(s, StaticWorldTile.Type.GRASS.getTile());
                            }

                            final int anchorFinal = anchor;
                            final int startSection = sectionState.get(clickContext);
                            clickContext.openForPlayer(PlaceMenu.class, ImmutableMap.of(
                                    "TILE_SIZE", building.getSize(),
                                    "START_SECTION", startSection,
                                    "START_ANCHOR", anchor,
                                    "TITLE", Msg.format("Edit"),
                                    "ON_PLACE", (PlaceMenu.PlaceAction) (ctx, newSection, newAnchor) -> {
                                        User uu = ctx.getUser();
                                        for (Integer s2 : building.getSize().toList(newAnchor)) {
                                            uu.getWorld().getSection(newSection).setSlot(s2, building.getTile());
                                        }
                                        uu.getPurchasedBuildings().getPurchasedBuilding(buildingTile.getBuildingType(), building.getSlot()).ifPresent(pb -> {
                                            pb.setPlaced(true);
                                            pb.setSection(newSection);
                                        });
                                        uu.recalculatePopulation();
                                        ctx.openForPlayer(WorldMenu.class, newSection);
                                    },
                                    "ON_CANCEL", (PlaceMenu.CancelAction) cancelCtx -> {
                                        User uu = cancelCtx.getUser();
                                        for (Integer s2 : building.getSize().toList(anchorFinal)) {
                                            uu.getWorld().getSection(startSection).setSlot(s2, building.getTile());
                                        }
                                        Towncraft.getViewFrame().open(WorldMenu.class, cancelCtx.getPlayer(), startSection);
                                    }
                            ));
                            openBackMenu.set(false, clickContext);
                            return;
                        }
                    }
                    clickContext.openForPlayer(WorldEditMenu.class, sectionState.get(clickContext));
                    openBackMenu.set(false, clickContext);
                    return;
                }
                if (tile.onClick(clickContext, section, slot)) {
                    openBackMenu.set(false, clickContext);
                }
            });
        });

        WorldUtils.applyArrows(player, sectionState.get(context));

        TowncraftItemStack profileItemStack = TowncraftItemStack.of(TowncraftMaterial.LIGHT_BLUE_CONCRETE);
        profileItemStack.setName(Msg.format("<green>%s", user.getTownName()));
        player.getInventory().setItem(22, profileItemStack);

        TowncraftItemStack levelAndPopStack = TowncraftItemStack.of(TowncraftMaterial.BLUE_CONCRETE);
        levelAndPopStack.setName(Msg.format("<aqua>Level %d", user.getLevel()));
        if (Towncraft.getDataLoader(LevelDataLoader.class).get(user.getLevel() + 1) != null) {
            levelAndPopStack.setLore(List.of(
                    Msg.format("<aqua>Xp %d", user.getXp()),
                    getLevelProgressBar(user),
                    Msg.format("<red>Population %d/%d", user.getPopulation(), user.getMaxPopulation())
            ));
        } else {
            levelAndPopStack.setLore(List.of(
                    Msg.format("<dark_red>You have reached the max level!"),
                    Msg.format("<red>Population %d/%d", user.getPopulation(), user.getMaxPopulation())
            ));
        }
        player.getInventory().setItem(9, levelAndPopStack);

        TowncraftItemStack coinsAndCashStack = TowncraftItemStack.of(TowncraftMaterial.GOLD_BLOCK);
        coinsAndCashStack.setName(Msg.format("<yellow>Coins %d", user.getCoins()));
        coinsAndCashStack.setLore(List.of(Msg.format("<green>Cash %d", user.getCash())));
        player.getInventory().setItem(17, coinsAndCashStack);

        TowncraftItemStack buildingMenuStack = TowncraftItemStack.of(TowncraftMaterial.YELLOW_CONCRETE);
        buildingMenuStack.setName(Msg.format("<white>Build Menu"));
        player.getInventory().setItem(8, buildingMenuStack);

        TowncraftItemStack editModeStack = TowncraftItemStack.of(TowncraftMaterial.IRON_AXE);
        editModeStack.setName(Msg.format("<white>Edit Mode"));
        player.getInventory().setItem(7, editModeStack);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            if (context.getClickedSlot() == 68 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) + 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 76 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) + 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 66 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) - 1);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 58 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) - 8);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 89 && context.getItem() != null) {
                context.openForPlayer(BuildMenu.class);
                openBackMenu.set(false, context);
            } else if (context.getClickedSlot() == 88 && context.getItem() != null) {
                context.openForPlayer(WorldEditMenu.class, sectionState.get(context));
                openBackMenu.set(false, context);
            }
        }
    }

    private Component getLevelProgressBar(User user) {
        LevelDataLoader.Level nextLevel = Towncraft.getDataLoader(LevelDataLoader.class).get(user.getLevel() + 1);
        if (nextLevel != null) {
            int max = nextLevel.getXpNeeded();
            float percent = max > 0 ? Math.min(1f, Math.max(0f, (float) user.getXp() / max)) : 0f;
            int progressBars = Math.max(0, Math.min(16, (int) (16 * percent)));

            return Msg.format(Strings.repeat("<aqua>■", progressBars) + Strings.repeat("<gray>■", 16 - progressBars));
        } else {
            return Msg.format("<dark_red>You have reached the max level!");
        }
    }
}
