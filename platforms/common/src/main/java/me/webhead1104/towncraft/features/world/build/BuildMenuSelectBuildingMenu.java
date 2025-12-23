package me.webhead1104.towncraft.features.world.build;

import com.google.common.collect.ImmutableMap;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.component.Pagination;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.features.world.PlaceMenu;
import me.webhead1104.towncraft.features.world.WorldMenu;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class BuildMenuSelectBuildingMenu extends TowncraftView {
    private final State<Key> keyState = initialState();
    private final State<BuildMenuType.BuildMenu> typeState = computedState(context -> Towncraft.getDataLoader(BuildMenuType.class).get(keyState.get(context)));
    private final State<Pagination> paginationState = buildComputedPaginationState(context -> typeState.get(context).getBuildings()).elementFactory((context, builder, index, buildingType) -> {
        BuildingType.Building building = context.getUser().getPurchasedBuildings().getNextBuilding(buildingType);
        if (building == null) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
            itemStack.setName(Msg.format("<red>You have purchased the max amount of this building!"));
            builder.withItem(itemStack);
            return;
        }

        TowncraftItemStack itemStack = building.getItemStack(userState.get(context));
        builder.withItem(itemStack);
        builder.onClick(slotClickContext -> {
            if (building.isNeedToBePlaced()) {
                BuildMenuType.BuildMenu type = typeState.get(slotClickContext);
                int startSection = slotClickContext.getUser().getSection();
                slotClickContext.openForPlayer(PlaceMenu.class, ImmutableMap.of(
                        "TILE_SIZE", building.getSize(),
                        "START_SECTION", startSection,
                        "TITLE", Msg.format("Place building"),
                        "ON_PLACE", (PlaceMenu.PlaceAction) (ctx, section, anchor) -> {
                            User user = userState.get(ctx);
                            for (Integer s : building.getSize().toList(anchor)) {
                                user.getWorld().getSection(section).setSlot(s, building.getTile());
                            }
                            user.getPurchasedBuildings().getPurchasedBuilding(buildingType, building.getSlot()).ifPresent(pb -> {
                                pb.setPlaced(true);
                                pb.setSection(section);
                            });
                            user.recalculatePopulation();
                            ctx.openForPlayer(WorldMenu.class, section);
                        },
                        "ON_CANCEL", (PlaceMenu.CancelAction) cancelCtx -> Towncraft.getViewFrame().open(BuildMenuSelectBuildingMenu.class, cancelCtx.getPlayer(), type)
                ));
                openBackMenu.set(false, slotClickContext);
                return;
            }
            User user = userState.get(slotClickContext);
            if (building.getPrice() == null) {
                throw new NullPointerException("price");
            }
            if (building.getPrice().has(user) &&
                    user.getLevel() >= building.getLevelNeeded() &&
                    user.getPopulation() >= building.getPopulationNeeded() &&
                    (user.getPopulation() + building.getPopulationIncrease()) <= user.getMaxPopulation()) {
                building.getPrice().take(user);
                user.getPurchasedBuildings().purchase(buildingType, building.getSlot());
                BuildMenuType.BuildMenu type = typeState.get(slotClickContext);
                int startSection = user.getSection();
                slotClickContext.openForPlayer(PlaceMenu.class, ImmutableMap.of("TILE_SIZE", building.getSize(), "START_SECTION", startSection, "TITLE", Msg.format("Place building"),
                        "ON_PLACE", (PlaceMenu.PlaceAction) (ctx, section, anchor) -> {
                            User u = ctx.getUser();
                            for (Integer s : building.getSize().toList(anchor)) {
                                u.getWorld().getSection(section).setSlot(s, building.getTile());
                            }
                            u.getPurchasedBuildings().getPurchasedBuilding(buildingType, building.getSlot()).ifPresent(pb -> {
                                pb.setPlaced(true);
                                pb.setSection(section);
                            });
                            u.recalculatePopulation();
                            ctx.openForPlayer(WorldMenu.class, section);
                        }, "ON_CANCEL", (PlaceMenu.CancelAction) cancelCtx -> Towncraft.getViewFrame().open(BuildMenuSelectBuildingMenu.class, cancelCtx.getPlayer(), type.key())));
                openBackMenu.set(false, slotClickContext);
            }
        });
    }).layoutTarget('B').build();

    public BuildMenuSelectBuildingMenu() {
        super(BuildMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(2);
        config.title("Build Menu");
        config.layout(
                "<BBBBBBB>",
                "b********");
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Pagination pagination = paginationState.get(context);
        context.layoutSlot('b').onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
            itemStack.setName(Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);

        context.layoutSlot('>').onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<white>Next Page"));
            slotRenderContext.setItem(itemStack);
        }).updateOnStateChange(paginationState).displayIf(() -> pagination.currentPageIndex() < pagination.lastPageIndex()).onClick(pagination::advance);

        context.layoutSlot('<').onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<white>Previous Page"));
            slotRenderContext.setItem(itemStack);
        }).displayIf(() -> pagination.currentPageIndex() != 0).onClick(pagination::back);
    }
}
