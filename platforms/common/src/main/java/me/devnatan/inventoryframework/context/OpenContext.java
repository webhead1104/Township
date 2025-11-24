package me.devnatan.inventoryframework.context;

import me.devnatan.inventoryframework.*;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.menus.TowncraftViewer;
import me.webhead1104.towncraft.menus.context.Context;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OpenContext extends PlatformConfinedContext implements IFOpenContext, Context {

    // --- Inherited ---
    private final UUID id;
    private final View root;
    private final Viewer subject;
    private final Map<String, Viewer> viewers;
    // --- Properties ---
    private final TowncraftPlayer player;
    private ViewContainer container;
    private Object initialData;
    // --- User Provided ---
    private CompletableFuture<Void> waitTask;
    private ViewConfigBuilder inheritedConfigBuilder;
    private boolean cancelled = false;

    /**
     * Creates a new open context instance.
     * <p>
     * <b><i> This is an internal inventory-framework API that should not be used from outside of
     * this library. No compatibility guarantees are provided. </i></b>
     *
     * @param root        Root view that will be owner of the upcoming render context.
     * @param subject     The viewer that is opening the view.
     * @param viewers     Who'll be the viewers of this context, if this parameter is provided it
     *                    means that this context is a shared context.
     *                    Must be provided even in non-shared context cases.
     * @param initialData Initial data provided by the user.
     */
    @ApiStatus.Internal
    public OpenContext(
            @NotNull View root, @Nullable Viewer subject, @NotNull Map<String, Viewer> viewers, Object initialData) {
        this.id = UUID.randomUUID();
        this.subject = subject;
        this.root = root;
        this.viewers = viewers;
        this.initialData = initialData;
        this.player = subject == null ? null : ((TowncraftViewer) subject).getPlayer();
    }

    /**
     * The player that's currently opening the view.
     *
     * @return The player that is opening the view.
     * @throws UnsupportedOperationInSharedContextException If this context {@link #isShared() is shared}.
     */
    public final @NotNull TowncraftPlayer getPlayer() {
        tryThrowDoNotWorkWithSharedContext("getAllPlayers()");
        return player;
    }

    public @NotNull User getUser() {
        tryThrowDoNotWorkWithSharedContext("getUser");
        return getPlayer().getUser();
    }

    @Override
    public List<TowncraftPlayer> getAllPlayers() {
        return getViewers().stream()
                .map(viewer -> (TowncraftViewer) viewer)
                .map(TowncraftViewer::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTitleForPlayer(@NotNull String title, @NotNull TowncraftPlayer player) {
        tryThrowDoNotWorkWithSharedContext();
        modifyConfig().title(title);
    }

    @Override
    public void resetTitleForPlayer(@NotNull TowncraftPlayer player) {
        tryThrowDoNotWorkWithSharedContext();
        if (getModifiedConfig() == null) return;

        modifyConfig().title(null);
    }

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public final CompletableFuture<Void> getAsyncOpenJob() {
        return waitTask;
    }

    @Override
    public final @NotNull View getRoot() {
        return root;
    }

    @Override
    public final @NotNull Map<String, Viewer> getIndexedViewers() {
        return viewers;
    }

    @Override
    public final @NotNull UUID getId() {
        return id;
    }

    @Override
    public final Object getInitialData() {
        return initialData;
    }

    @Override
    public void setInitialData(Object initialData) {
        this.initialData = initialData;
    }

    @Override
    public final void waitUntil(@NotNull CompletableFuture<Void> task) {
        this.waitTask = task;
    }

    @Override
    public final @NotNull ViewConfig getConfig() {
        return inheritedConfigBuilder == null
                ? getRoot().getConfig()
                : Objects.requireNonNull(getModifiedConfig(), "Modified config cannot be null");
    }

    @Override
    public final ViewConfig getModifiedConfig() {
        if (inheritedConfigBuilder == null) return null;

        return inheritedConfigBuilder.build().merge(getRoot().getConfig());
    }

    @Override
    public final @NotNull ViewConfigBuilder modifyConfig() {
        if (inheritedConfigBuilder == null) inheritedConfigBuilder = new ViewConfigBuilder();

        return inheritedConfigBuilder;
    }

    @Override
    public Viewer getViewer() {
        tryThrowDoNotWorkWithSharedContext("getViewers()");
        return subject;
    }

    @Override
    public ViewContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(ViewContainer container) {
        this.container = container;
    }
}
