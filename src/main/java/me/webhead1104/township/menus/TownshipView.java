package me.webhead1104.township.menus;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import org.jetbrains.annotations.NotNull;

public class TownshipView extends View {
    protected final Class<? extends View> closeClass;
    protected final MutableState<Boolean> openBackMenu = mutableState(true);
    protected final State<User> userState = computedState(context -> Township.getUserManager().getUser(context.getPlayer().getUniqueId()));
    protected State<Object> initialData = computedState(context -> Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getSection());

    public TownshipView(Class<? extends View> closeClass) {
        this.closeClass = closeClass;
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
                    Township.getViewFrame().open(closeClass, context.getPlayer());
                } else {
                    Township.getViewFrame().open(closeClass, context.getPlayer(), initialData.get(context));
                }
            }
        });
    }
}
