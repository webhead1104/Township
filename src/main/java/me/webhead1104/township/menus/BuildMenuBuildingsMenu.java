package me.webhead1104.township.menus;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildMenuType;
import me.webhead1104.township.data.enums.BuildingType;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildMenuBuildingsMenu extends View {
    private final State<BuildMenuType> type = initialState("TYPE");
    private final State<Integer> page = initialState("PAGE");
    private final MutableState<Boolean> openBuildMenu = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(18);
        config.title("Build Menu");
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openBuildMenu.get(context)) {
                Township.getViewFrame().open(BuildMenu.class, context.getPlayer());
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        BuildMenuType type = this.type.get(context);
        int page = this.page.get(context);
        if (type.getBuildings(page) == null) {
            context.closeForPlayer();
            return;
        }
        context.slot(9).onRender(slotRenderContext -> {
            ItemStack itemStack = ItemStack.of(Material.BARRIER);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);

        Map<Integer, List<Building>> map = new HashMap<>();
        for (BuildingType buildingType : type.getBuildings(page)) {
            map.put(buildingType.ordinal(), new ArrayList<>());
            for (Building value : buildingType.getBuildings().values()) {
                map.get(buildingType.ordinal()).add(value);
            }
        }
        context.slot(8).onRender(slotRenderContext -> {
            if (map.get(page).size() < 7) return;
            ItemStack itemStack = ItemStack.of(Material.ARROW);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Next Page"));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            if (slotClickContext.getItem() == null) return;
            slotClickContext.openForPlayer(BuildMenuBuildingsMenu.class, ImmutableMap.of("TYPE", type, "PAGE", page + 1));
            openBuildMenu.set(false, slotClickContext);
        });

        context.slot(0).onRender(slotRenderContext -> {
            if (page == 0) return;
            ItemStack itemStack = ItemStack.of(Material.ARROW);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Previous Page"));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            if (slotClickContext.getItem() == null) return;
            slotClickContext.openForPlayer(BuildMenuBuildingsMenu.class, ImmutableMap.of("TYPE", type, "PAGE", page - 1));
            openBuildMenu.set(false, slotClickContext);
        });
        User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
        AtomicInteger slot = new AtomicInteger(1);
        map.forEach((buildingTypeSlot, buildings) -> {
            BuildingType buildingType = BuildingType.values()[buildingTypeSlot];
            int buildingSlot;
            if (buildings.size() > 1) {
                buildingSlot = user.getPurchasedBuildings().amountPurchased(buildingType) + 1;
            } else {
                buildingSlot = 0;
            }
            context.slot(slot.getAndIncrement()).onRender(slotRenderContext -> {
                ItemStack itemStack = buildings.get(buildingSlot).getItemStack(context.getPlayer());
                itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                        Msg.format("<green>%s/%s purchased", user.getPurchasedBuildings().amountPurchased(buildingType), buildings.size()))));
                slotRenderContext.setItem(itemStack);
            }).onClick(slotClickContext -> {
                if (user.getPurchasedBuildings().isPurchased(buildingType, buildingSlot)) {
                }
            });
        });
    }
}
