package me.webhead1104.towncraft.features.world;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.*;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.TileSize;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.CloseContext;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.tiles.StaticWorldTile;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class PlaceMenu extends View {

    private final MutableIntState sectionState = mutableState(27);
    private final State<TileSize> tileSizeState = initialState("TILE_SIZE");
    private final State<Integer> startSectionState = initialState("START_SECTION");
    private final State<PlaceAction> onPlaceState = initialState("ON_PLACE");
    private final State<CancelAction> onCancelState = initialState("ON_CANCEL");
    private final State<Integer> startAnchorState = initialState("START_ANCHOR");
    private final State<Component> titleState = initialState("TITLE");
    private final MutableIntState slotState = mutableState(0);
    private final MutableState<Boolean> canPlaceState = mutableState(false);
    private final MutableState<Boolean> placedState = mutableState(false);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(1);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.modifyConfig().title(titleState.get(context));
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(TowncraftItemStack.empty());
        Integer start = startSectionState.get(context);
        if (start != null) {
            sectionState.set(start, context);
        } else {
            int current = context.getPlayer().getUser().getSection();
            sectionState.set(current, context);
        }
        Integer startAnchor = startAnchorState.get(context);
        if (startAnchor != null) {
            TileSize size = tileSizeState.get(context);
            int adjusted = WorldUtils.adjustPlacement(startAnchor, size);
            slotState.set(adjusted, context);
            canPlaceState.set(true, context);
        }
        context.update();
    }

    @Override
    public void onUpdate(@NotNull Context context) {
        WorldUtils.applyArrows(context.getPlayer(), sectionState.get(context));
        placeConfirm(canPlaceState.get(context), context.getPlayer());
    }

    private void placeConfirm(boolean canPlace, TowncraftPlayer player) {
        TowncraftItemStack confirmItemStack;
        if (canPlace) {
            confirmItemStack = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
            confirmItemStack.setName(Msg.format("<green>Click to place it here!"));
        } else {
            confirmItemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            confirmItemStack.setName(Msg.format("<red>You can't place it here!"));
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

        User user = context.getUser();
        for (int i = 0; i < 54; i++) {
            final int idx = i;
            context.slot(i).updateOnClick().watch(sectionState).onRender(slotRenderContext -> {
                WorldSection ws = user.getWorld().getSection(sectionState.get(context));
                TileSize size = tileSizeState.get(context);
                if (size.toList(slotState.get(slotRenderContext)).contains(idx)) {
                    if (ws.getSlot(idx) instanceof StaticWorldTile) {
                        TowncraftItemStack ok = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
                        ok.setName(Msg.format("<green>You can place it here!"));
                        slotRenderContext.setItem(ok);
                    } else {
                        canPlaceState.set(false, context);
                        TowncraftItemStack bad = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
                        bad.setName(Msg.format("<red>You can't place it here!"));
                        slotRenderContext.setItem(bad);
                    }
                    return;
                }
                slotRenderContext.setItem(ws.getSlot(idx).render(slotRenderContext, user.getWorld().getSection(sectionState.get(context)), idx));
            }).onClick(click -> {
                TileSize size = tileSizeState.get(click);
                int newSlot = WorldUtils.adjustPlacement(click.getClickedSlot(), size);
                if (!WorldUtils.canPlace(newSlot, size)) return;
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
        if (context.getClickedSlot() == 68 && context.itemExists()) {
            sectionState.set(section + 1, context);
            canPlaceState.set(true, context);
            context.update();
        } else if (context.getClickedSlot() == 76 && context.itemExists()) {
            sectionState.set(section + 8, context);
            canPlaceState.set(true, context);
            context.update();
        } else if (context.getClickedSlot() == 66 && context.itemExists()) {
            sectionState.set(section - 1, context);
            canPlaceState.set(true, context);
            context.update();
        } else if (context.getClickedSlot() == 58 && context.itemExists()) {
            sectionState.set(section - 8, context);
            canPlaceState.set(true, context);
            context.update();
        } else if (context.getClickedSlot() == 67 && context.itemExists() && Boolean.TRUE.equals(canPlaceState.get(context))) {
            PlaceAction place = onPlaceState.get(context);
            if (place != null) {
                placedState.set(true, context);
                place.onPlace(context, section, slotState.get(context));
            }
        }
    }

    public interface PlaceAction {
        void onPlace(SlotClickContext context, int section, int anchor);
    }

    public interface CancelAction {
        void onCancel(CloseContext context);
    }
}
