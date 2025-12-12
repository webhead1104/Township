package me.webhead1104.towncraft.menus;

import lombok.NoArgsConstructor;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.features.world.WorldMenu;
import me.webhead1104.towncraft.menus.context.CloseContext;
import me.webhead1104.towncraft.menus.context.Context;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class TowncraftView extends View {
    protected final MutableState<Boolean> openBackMenu = mutableState(true);
    protected final State<User> userState = computedState(Context::getUser);
    protected MutableState<Class<? extends View>> closeClass = mutableState(WorldMenu.class);
    protected State<Object> initialData = computedState(context -> context.getUser().getSection());

    public TowncraftView(Class<? extends View> closeClass) {
        this.closeClass = mutableState(closeClass);
        if (!closeClass.equals(WorldMenu.class)) {
            initialData = null;
        }
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        nextTick(() -> {
            if (openBackMenu.get(context)) {
                if (initialData == null) {
                    Towncraft.getViewFrame().open(closeClass.get(context), context.getPlayer());
                } else {
                    Towncraft.getViewFrame().open(closeClass.get(context), context.getPlayer(), initialData.get(context));
                }
            }
        });
    }
}
