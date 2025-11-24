package me.webhead1104.towncraft.menus.context;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfig;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.Viewer;
import me.devnatan.inventoryframework.context.IFCloseContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.PlatformConfinedContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.devnatan.inventoryframework.state.StateValue;
import me.devnatan.inventoryframework.state.StateWatcher;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.events.TowncraftInventoryCloseEvent;
import me.webhead1104.towncraft.menus.TowncraftViewer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CloseContext extends PlatformConfinedContext implements IFCloseContext, Context {

    private final Viewer subject;
    private final TowncraftPlayer player;
    private final IFRenderContext parent;
    private final TowncraftInventoryCloseEvent closeOrigin;

    private boolean cancelled;

    @ApiStatus.Internal
    public CloseContext(
            @NotNull Viewer subject, @NotNull IFRenderContext parent, @NotNull TowncraftInventoryCloseEvent closeOrigin) {
        this.subject = subject;
        this.player = ((TowncraftViewer) subject).getPlayer();
        this.parent = parent;
        this.closeOrigin = closeOrigin;
    }

    @Override
    public Object getPlatformEvent() {
        return closeOrigin;
    }

    // TODO Needs documentation
    public final @NotNull TowncraftPlayer getPlayer() {
        return player;
    }

    public final @NotNull User getUser() {
        return player.getUser();
    }

    @Override
    public List<TowncraftPlayer> getAllPlayers() {
        return getParent().getAllPlayers();
    }

    @Override
    public void updateTitleForPlayer(@NotNull String title, @NotNull TowncraftPlayer player) {
        getParent().updateTitleForPlayer(title, player);
    }

    @Override
    public void resetTitleForPlayer(@NotNull TowncraftPlayer player) {
        getParent().resetTitleForPlayer(player);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public final @NotNull Viewer getViewer() {
        return subject;
    }

    @Override
    public final RenderContext getParent() {
        return (RenderContext) parent;
    }

    @Override
    public final @NotNull UUID getId() {
        return getParent().getId();
    }

    @Override
    public final @NotNull ViewConfig getConfig() {
        return getParent().getConfig();
    }

    @Override
    public final @NotNull ViewContainer getContainer() {
        return getParent().getContainer();
    }

    @Override
    public final @NotNull View getRoot() {
        return getParent().getRoot();
    }

    @Override
    public final Object getInitialData() {
        return getParent().getInitialData();
    }

    @Override
    public void setInitialData(Object initialData) {
        getParent().setInitialData(initialData);
    }

    @Override
    public final @UnmodifiableView Map<Long, StateValue> getStateValues() {
        return getParent().getStateValues();
    }

    @Override
    public final void initializeState(long id, @NotNull StateValue value) {
        getParent().initializeState(id, value);
    }

    @Override
    public final void watchState(long id, StateWatcher listener) {
        getParent().watchState(id, listener);
    }

    @Override
    public final Object getRawStateValue(State<?> state) {
        return getParent().getRawStateValue(state);
    }

    @Override
    public final StateValue getInternalStateValue(State<?> state) {
        return getParent().getInternalStateValue(state);
    }

    @Override
    public final StateValue getUninitializedStateValue(long stateId) {
        return getParent().getUninitializedStateValue(stateId);
    }

    @Override
    public final void updateState(State<?> state, Object value) {
        getParent().updateState(state, value);
    }

    @Override
    public String toString() {
        return "CloseContext{" + "subject="
                + subject + ", player="
                + player + ", parent="
                + parent + ", cancelled="
                + cancelled + "} "
                + super.toString();
    }
}
