package me.webhead1104.township.menus;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import org.jetbrains.annotations.NotNull;

public class TownshipView extends View {
    protected final Class<? extends View> closeClass;
    protected final MutableState<Boolean> openBackMenu = mutableState(true);
    protected State<Object> initalData = computedState(context -> Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getSection());

    public TownshipView(Class<? extends View> closeClass) {
        this.closeClass = closeClass;
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        nextTick(() -> {
            if (openBackMenu.get(context)) {
                if (initalData.get(context) == null) {
                    context.openForPlayer(closeClass);
                } else {
                    context.openForPlayer(closeClass, initalData.get(context));
                }
            }
        });
    }
}
