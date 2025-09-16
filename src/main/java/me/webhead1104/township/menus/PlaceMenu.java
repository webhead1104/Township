package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.*;
import me.devnatan.inventoryframework.state.*;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.WorldSection;
import me.webhead1104.township.data.enums.TileSize;
import me.webhead1104.township.tiles.StaticWorldTile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Generic placement view that highlights a target TileSize and lets caller provide actions.
 * Required initial states when opening:
 * - TILE_SIZE: TileSize (required)
 * - START_SECTION: Integer (optional; defaults to current user's section)
 * - ON_PLACE: PlaceAction (required) — called when player confirms placement; receives (context, section, anchor)
 * - ON_CANCEL: CancelAction (optional) — called if view is closed without confirming
 */
public class PlaceMenu extends View {

    public interface PlaceAction {
        void onPlace(SlotClickContext context, int section, int anchor);
    }

    public interface CancelAction {
        void onCancel(CloseContext context);
    }

    private final MutableIntState sectionState = mutableState(27);
    private final State<TileSize> tileSizeState = initialState("TILE_SIZE");
    private final State<Integer> startSectionState = initialState("START_SECTION");
    private final State<PlaceAction> onPlaceState = initialState("ON_PLACE");
    private final State<CancelAction> onCancelState = initialState("ON_CANCEL");

    private final MutableIntState slotState = mutableState(0);
    private final MutableState<Boolean> canPlaceState = mutableState(false);
    private final MutableState<Boolean> placedState = mutableState(false);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(1);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Place"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
        Integer start = startSectionState.get(context);
        if (start != null) {
            sectionState.set(start, context);
        } else {
            // fallback to user's current section
            int current = Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getSection();
            sectionState.set(current, context);
        }
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
        if (!Boolean.TRUE.equals(placedState.get(context))) {
            CancelAction cancel = onCancelState.get(context);
            if (cancel != null) {
                cancel.onCancel(context);
            }
        }
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        // Set canPlace true upon section change
        context.watchState(sectionState.internalId(), new StateWatcher() {
            @Override public void stateRegistered(@NotNull State<?> state, Object caller) {}
            @Override public void stateUnregistered(@NotNull State<?> state, Object caller) {}
            @Override public void stateValueGet(@NotNull State<?> state, @NotNull StateValueHost host, @NotNull StateValue internalValue, Object rawValue) {}
            @Override public void stateValueSet(@NotNull StateValueHost host, @NotNull StateValue value, Object rawOldValue, Object rawNewValue) {
                canPlaceState.set(true, host);
            }
        });

        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        for (int i = 0; i < 54; i++) {
            final int idx = i;
            context.slot(i).updateOnClick().watch(sectionState).onRender(slotRenderContext -> {
                WorldSection ws = user.getWorld().getSection(sectionState.get(context));
                TileSize size = tileSizeState.get(context);
                if (size.toList(slotState.get(slotRenderContext)).contains(idx)) {
                    if (ws.getSlot(idx) instanceof StaticWorldTile) {
                        ItemStack ok = ItemStack.of(Material.LIME_CONCRETE);
                        ok.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>You can place it here!"));
                        slotRenderContext.setItem(ok);
                    } else {
                        canPlaceState.set(false, context);
                        ItemStack bad = ItemStack.of(Material.RED_CONCRETE);
                        bad.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>You can't place it here!"));
                        slotRenderContext.setItem(bad);
                    }
                    return;
                }
                slotRenderContext.setItem(ws.getSlot(idx).render(slotRenderContext));
            }).onClick(click -> {
                TileSize size = tileSizeState.get(click);
                int newSlot = Township.getWorldManager().adjustPlacement(click.getClickedSlot(), size);
                if (!Township.getWorldManager().canPlace(newSlot, size)) return;
                slotState.set(newSlot, click);
                sectionState.set(sectionState.get(click), click);
                click.update();
            });
        }
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
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
        } else if (context.getClickedSlot() == 67 && context.getItem() != null && Boolean.TRUE.equals(canPlaceState.get(context))) {
            PlaceAction place = onPlaceState.get(context);
            if (place != null) {
                placedState.set(true, context);
                place.onPlace(context, section, slotState.get(context));
            }
        }
    }
}
