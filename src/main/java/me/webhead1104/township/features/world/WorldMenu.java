package me.webhead1104.township.features.world;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.dataLoaders.BuildingType;
import me.webhead1104.township.dataLoaders.LevelDataLoader;
import me.webhead1104.township.features.world.build.BuildMenu;
import me.webhead1104.township.features.world.edit.WorldEditMenu;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.tiles.BuildingTile;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldMenu extends TownshipView {
    private final State<Integer> sectionState = initialState();

    public WorldMenu() {
        super(ConfirmCloseMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("World Menu"));
        initialData = null;
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
        world.getSection(sectionState.get(context)).getSlotMap().forEach((key, tile) ->
                context.slot(key).updateOnClick().onUpdate(slotContext -> {
                            if (tile.onUpdate(slotContext, world.getSection(sectionState.get(slotContext)), key)) {
                                openBackMenu.set(false, slotContext);
                            }
                        })
                        .onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
                            if (clickContext.isShiftRightClick()) {
                                if (tile.isImmovable()) return;
                                if (tile instanceof BuildingTile buildingTile) {
                                    BuildingType.Building building = BuildingType.get(buildingTile.getBuildingType()).get(buildingTile.getBuildingSlot());
                                    int clickedSlot = clickContext.getClickedSlot();
                                    WorldSection worldSection = Township.getUserManager().getUser(clickContext.getPlayer().getUniqueId()).getWorld().getSection(sectionState.get(clickContext));

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
                                                    User uu = Township.getUserManager().getUser(ctx.getPlayer().getUniqueId());
                                                    for (Integer s2 : building.getSize().toList(newAnchor)) {
                                                        uu.getWorld().getSection(newSection).setSlot(s2, building.getTile());
                                                    }
                                                    uu.getPurchasedBuildings().getPurchasedBuilding(buildingTile.getBuildingType(), building.getSlot()).ifPresent(pb -> {
                                                        pb.setPlaced(true);
                                                        pb.setSection(newSection);
                                                    });
                                                    uu.getPurchasedBuildings().recalculatePopulation(ctx.getPlayer());
                                                    ctx.openForPlayer(WorldMenu.class, newSection);
                                                },
                                                "ON_CANCEL", (PlaceMenu.CancelAction) cancelCtx -> {
                                                    User uu = Township.getUserManager().getUser(cancelCtx.getPlayer().getUniqueId());
                                                    for (Integer s2 : building.getSize().toList(anchorFinal)) {
                                                        uu.getWorld().getSection(startSection).setSlot(s2, building.getTile());
                                                    }
                                                    Township.getViewFrame().open(WorldMenu.class, cancelCtx.getPlayer(), startSection);
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
                            if (tile.onClick(clickContext)) {
                                openBackMenu.set(false, clickContext);
                            }
                        }));

        WorldUtils.applyArrows(player, sectionState.get(context));

        ItemStack profileItemStack = ItemStack.of(Material.LIGHT_BLUE_CONCRETE);
        profileItemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>%s", user.getTownName()));
        player.getInventory().setItem(22, profileItemStack);

        ItemStack levelAndPopStack = ItemStack.of(Material.BLUE_CONCRETE);
        levelAndPopStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<aqua>Level %d", user.getLevel()));
        if (LevelDataLoader.get(user.getLevel() + 1) != null) {
            levelAndPopStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<aqua>Xp %d", user.getXp()),
                    getLevelProgressBar(user),
                    Msg.format("<red>Population %d", user.getPopulation())
            )));
        } else {
            levelAndPopStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<dark_red>You have reached the max level!"),
                    Msg.format("<red>Population " + user.getPopulation())
            )));
        }
        player.getInventory().setItem(9, levelAndPopStack);

        ItemStack coinsAndCashStack = ItemStack.of(Material.GOLD_BLOCK);
        coinsAndCashStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<yellow>Coins %d", user.getCoins()));
        coinsAndCashStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>Cash %d", user.getCash()))));
        player.getInventory().setItem(17, coinsAndCashStack);

        ItemStack buildingMenuStack = ItemStack.of(Material.YELLOW_CONCRETE);
        buildingMenuStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Build Menu"));
        player.getInventory().setItem(8, buildingMenuStack);

        ItemStack editModeStack = ItemStack.of(Material.IRON_AXE);
        editModeStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Edit Mode"));
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
        LevelDataLoader.Level nextLevel = LevelDataLoader.get(user.getLevel() + 1);
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
