package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.*;
import me.devnatan.inventoryframework.state.*;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildMenuType;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BuildPlaceMenu extends View {
    private final MutableIntState sectionState = mutableState(27);
    private final State<BuildMenuType> buildMenuTypeState = initialState("BUILD_MENU_TYPE");
    private final State<Building> buildingState = initialState("BUILDING");
    private final MutableIntState slotState = mutableState(0);
    private final MutableState<Boolean> canPlaceState = mutableState(false);
    private final MutableState<Boolean> openBuildingSelectMenu = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(1);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Place building"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
        context.update();
    }

    @Override
    public void onUpdate(@NotNull Context context) {
        Township.getWorldManager().applyArrows(context.getPlayer(), sectionState.get(context));
        placeConfirm(canPlaceState.get(context), context.getPlayer());
    }

    private void placeConfirm(boolean canPlace, Player player) {
        ItemStack confirmItemStack;
        if (canPlace) {
            confirmItemStack = ItemStack.of(Material.LIME_CONCRETE);
            confirmItemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click to place it here!"));
        } else {
            confirmItemStack = ItemStack.of(Material.RED_CONCRETE);
            confirmItemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>You can't place it here!"));
        }

        player.getInventory().setItem(22, confirmItemStack);
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openBuildingSelectMenu.get(context)) {
                Township.getViewFrame().open(BuildMenuSelectBuildingMenu.class, context.getPlayer(), buildMenuTypeState.get(context));
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        context.watchState(sectionState.internalId(), new StateWatcher() {
            @Override
            public void stateRegistered(@NotNull State<?> state, Object caller) {
            }

            @Override
            public void stateUnregistered(@NotNull State<?> state, Object caller) {
            }

            @Override
            public void stateValueGet(@NotNull State<?> state, @NotNull StateValueHost host, @NotNull StateValue internalValue, Object rawValue) {
            }

            @Override
            public void stateValueSet(@NotNull StateValueHost host, @NotNull StateValue value, Object rawOldValue, Object rawNewValue) {
                canPlaceState.set(true, host);
            }
        });
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        for (int i = 0; i < 54; i++) {
            int finalI = i;
            context.slot(i).updateOnClick().watch(sectionState).onRender(slotRenderContext -> {
                WorldSection worldSection = user.getWorld().getSection(sectionState.get(context));
                if (buildingState.get(slotRenderContext).getSize().toList(slotState.get(slotRenderContext)).contains(finalI)) {
                    if (worldSection.getSlot(finalI) instanceof StaticWorldTile) {
                        ItemStack itemStack = ItemStack.of(Material.LIME_CONCRETE);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>You can place it here!"));
                        slotRenderContext.setItem(itemStack);
                    } else {
                        canPlaceState.set(false, context);
                        ItemStack itemStack = ItemStack.of(Material.RED_CONCRETE);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>You can't place it here!"));
                        slotRenderContext.setItem(itemStack);
                    }
                    return;
                }
                slotRenderContext.setItem(worldSection.getSlot(finalI).render(slotRenderContext));
            }).onClick(slotClickContext -> {
                int newSlot = Township.getWorldManager().adjustPlacement(slotClickContext.getClickedSlot(), buildingState.get(slotClickContext).getSize());
                if (!Township.getWorldManager().canPlace(newSlot, buildingState.get(slotClickContext).getSize()))
                    return;
                slotState.set(newSlot, slotClickContext);
                sectionState.set(sectionState.get(slotClickContext), slotClickContext);
                slotClickContext.update();
            });
        }
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            int section = sectionState.get(context);
            if (context.getClickedSlot() == 68 && context.getItem() != null) {
                sectionState.set(section + 1, context);
                canPlaceState.set(true, context);
                context.update();
            } else if (context.getClickedSlot() == 76 && context.getItem() != null) {
                sectionState.set(section + 8, context);
                canPlaceState.set(true, context);
                context.update();
            } else if (context.getClickedSlot() == 66 && context.getItem() != null) {
                sectionState.set(section - 1, context);
                canPlaceState.set(true, context);
                context.update();
            } else if (context.getClickedSlot() == 58 && context.getItem() != null) {
                sectionState.set(section - 8, context);
                canPlaceState.set(true, context);
                context.update();
            } else if (context.getClickedSlot() == 67 && context.getItem() != null && canPlaceState.get(context)) {
                Building building = buildingState.get(context);
                User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
                for (Integer slot : building.getSize().toList(slotState.get(context))) {
                    user.getWorld().getSection(sectionState.get(context)).setSlot(slot, building.getTile());
                }
                context.openForPlayer(WorldMenu.class, section);
                openBuildingSelectMenu.set(false, context);
            }
        }
    }
}