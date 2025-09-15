package me.webhead1104.township.menus;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.component.Pagination;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildMenuType;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildMenuSelectBuildingMenu extends View {
    private final State<BuildMenuType> typeState = initialState();
    private final MutableState<Boolean> openBuildMenuState = mutableState(true);
    private final State<Pagination> paginationState = buildComputedPaginationState(context -> typeState.get(context).getBuildings()).elementFactory((context, builder, index, buildingType) -> {
        Building building = buildingType.getNextBuilding(context.getPlayer());
        User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
        if (building == null) {
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>You have purchased the max amount of this building!"));
            builder.withItem(itemStack);
            return;
        }

        ItemStack itemStack = building.getItemStack(context.getPlayer());
        List<Component> components = new ArrayList<>(Objects.requireNonNull(itemStack.getData(DataComponentTypes.LORE)).lines());
        components.set(1, Msg.format("<green>%s/%s purchased", user.getPurchasedBuildings().amountPurchased(buildingType), buildingType.getBuildings().size()));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(components));
        builder.withItem(itemStack);
        builder.onClick(slotClickContext -> {
            if (building.isNeedToBePlaced()) {
                slotClickContext.openForPlayer(BuildPlaceMenu.class, ImmutableMap.of("BUILDING", building, "BUILDING_TYPE", buildingType, "BUILD_MENU_TYPE", typeState.get(slotClickContext)));
                openBuildMenuState.set(false, slotClickContext);
                return;
            }
            if (building.getPrice().has(slotClickContext.getPlayer()) &&
                    user.getLevel() >= building.getLevelNeeded() &&
                    user.getPopulation() >= building.getPopulationNeeded()) {
                building.getPrice().take(slotClickContext.getPlayer());
                user.getPurchasedBuildings().purchase(buildingType, building.getSlot());
                slotClickContext.openForPlayer(BuildPlaceMenu.class, ImmutableMap.of("BUILDING", building, "BUILDING_TYPE", buildingType, "BUILD_MENU_TYPE", typeState.get(slotClickContext)));
                openBuildMenuState.set(false, slotClickContext);
            }
        });
    }).layoutTarget('B').build();

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
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openBuildMenuState.get(context)) {
                Township.getViewFrame().open(BuildMenu.class, context.getPlayer());
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Pagination pagination = paginationState.get(context);
        context.layoutSlot('b').onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.BARRIER);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);

        context.layoutSlot('>').onRender(slotRenderContext -> {
                    ItemStack itemStack = ItemStack.of(Material.ARROW);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Next Page"));
                    slotRenderContext.setItem(itemStack);
                }).updateOnStateChange(paginationState)
                .displayIf(() -> pagination.currentPageIndex() < pagination.lastPageIndex())
                .onClick(pagination::advance);

        context.layoutSlot('<').onRender(slotRenderContext -> {
                    ItemStack itemStack = ItemStack.of(Material.ARROW);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Previous Page"));
                    slotRenderContext.setItem(itemStack);
                }).displayIf(() -> pagination.currentPageIndex() != 0)
                .onClick(pagination::back);
    }
}
