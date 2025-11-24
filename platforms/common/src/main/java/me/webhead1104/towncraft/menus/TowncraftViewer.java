package me.webhead1104.towncraft.menus;

import lombok.Getter;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.Viewer;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.webhead1104.towncraft.TowncraftPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public final class TowncraftViewer implements Viewer {

    @Getter
    private final TowncraftPlayer player;
    private final Deque<IFRenderContext> previousContexts = new LinkedList<>();
    private ViewContainer selfContainer;
    private IFRenderContext activeContext;
    private long lastInteractionInMillis;
    private boolean switching;

    public TowncraftViewer(@NotNull TowncraftPlayer player, IFRenderContext activeContext) {
        this.player = player;
        this.activeContext = activeContext;
    }

    @NotNull
    @Override
    public IFRenderContext getActiveContext() {
        return activeContext;
    }

    @Override
    public void setActiveContext(@NotNull IFRenderContext context) {
        this.activeContext = context;
    }

    @Override
    public @NotNull IFRenderContext getCurrentContext() {
        IFRenderContext prevCtx = null;
        if (isSwitching() && ((prevCtx = getPreviousContext()) == null))
            throw new IllegalStateException("Previous context cannot be null when switching");

        return prevCtx == null ? getActiveContext() : prevCtx;
    }

    @Override
    public @NotNull String getId() {
        return getPlayer().getUUID().toString();
    }

    @Override
    public void open(@NotNull final ViewContainer container) {
        getPlayer().openInventory(((TowncraftViewContainer) container).getInventory());
    }

    @Override
    public void close() {
        getPlayer().closeInventory();
    }

    @Override
    public @NotNull ViewContainer getSelfContainer() {
        if (selfContainer == null)
            selfContainer = new TowncraftViewContainer(
                    getPlayer().getInventory(), getActiveContext().isShared(), ViewType.PLAYER, false);

        return selfContainer;
    }

    @Override
    public long getLastInteractionInMillis() {
        return lastInteractionInMillis;
    }

    @Override
    public void setLastInteractionInMillis(long lastInteractionInMillis) {
        this.lastInteractionInMillis = lastInteractionInMillis;
    }

    @Override
    public boolean isBlockedByInteractionDelay() {
        final long configuredDelay = activeContext.getConfig().getInteractionDelayInMillis();
        if (configuredDelay <= 0 || getLastInteractionInMillis() <= 0) return false;

        return getLastInteractionInMillis() + configuredDelay >= System.currentTimeMillis();
    }

    @Override
    public boolean isSwitching() {
        return switching;
    }

    @Override
    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    @Override
    public IFRenderContext getPreviousContext() {
        return previousContexts.peekLast();
    }

    @Override
    public void setPreviousContext(IFRenderContext previousContext) {
        previousContexts.pollLast();
        previousContexts.add(previousContext);
    }

    @Override
    public Object getPlatformInstance() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowncraftViewer that = (TowncraftViewer) o;
        return Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer());
    }

    @Override
    public String toString() {
        return "BukkitViewer{"
                + "player=" + player
                + ", selfContainer=" + selfContainer
                + ", lastInteractionInMillis=" + lastInteractionInMillis
                + ", isTransitioning=" + switching
                + "}";
    }
}
