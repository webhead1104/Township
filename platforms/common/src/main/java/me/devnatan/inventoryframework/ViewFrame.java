package me.devnatan.inventoryframework;

import me.devnatan.inventoryframework.context.EndlessContextInfo;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.feature.DefaultFeatureInstaller;
import me.devnatan.inventoryframework.feature.Feature;
import me.devnatan.inventoryframework.feature.FeatureInstaller;
import me.devnatan.inventoryframework.internal.PlatformUtils;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.menus.internal.TowncraftElementFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ViewFrame extends IFViewFrame<ViewFrame, View> {
    private final FeatureInstaller<ViewFrame> featureInstaller = new DefaultFeatureInstaller<>(this);

    /**
     * Opens a view to a player.
     *
     * @param viewClass The target view to be opened.
     * @param player    The player that the view will be open to.
     * @return The id of the newly created {@link IFContext}.
     */
    public final String open(@NotNull Class<? extends View> viewClass, @NotNull TowncraftPlayer player) {
        return open(viewClass, player, null);
    }

    /**
     * Opens a view to a player with initial data.
     *
     * @param viewClass   The target view to be opened.
     * @param player      The player that the view will be open to.
     * @param initialData The initial data.
     * @return The id of the newly created {@link IFContext}.
     */
    public final String open(@NotNull Class<? extends View> viewClass, @NotNull TowncraftPlayer player, Object initialData) {
        return open(viewClass, Collections.singletonList(player), initialData);
    }

    /**
     * Opens a view to more than one player.
     * <p>
     * These players will see the same inventory and share the same context.
     *
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param viewClass The target view to be opened.
     * @param players   The players that the view will be open to.
     * @return The id of the newly created {@link IFContext}.
     */
    @ApiStatus.Experimental
    public final String open(@NotNull Class<? extends View> viewClass, @NotNull Collection<? extends TowncraftPlayer> players) {
        return open(viewClass, players, null);
    }

    /**
     * Opens a view to more than one player with initial data.
     * <p>
     * These players will see the same inventory and share the same context.
     *
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param viewClass   The target view to be opened.
     * @param players     The players that the view will be open to.
     * @param initialData The initial data.
     * @return The id of the newly created {@link IFContext}.
     */
    @ApiStatus.Experimental
    public final String open(
            @NotNull Class<? extends View> viewClass,
            @NotNull Collection<? extends TowncraftPlayer> players,
            Object initialData) {
        return internalOpen(
                viewClass,
                players.stream()
                        .collect(Collectors.toMap(player -> player.getUUID().toString(), Function.identity())),
                initialData);
    }

    /**
     * Opens an already active context to a player.
     * <p>
     * <b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param contextId The id of the context.
     * @param player    Who the context will be open to.
     */
    @ApiStatus.Experimental
    public final void openActive(
            @NotNull Class<? extends View> viewClass, @NotNull String contextId, @NotNull TowncraftPlayer player) {
        openActive(viewClass, contextId, player, null);
    }

    /**
     * Opens an already active context to a player.
     * <p>
     * <b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param contextId   The id of the context.
     * @param player      Who the context will be open to.
     * @param initialData Initial data to pass to {@link PlatformView#onViewerAdded(IFContext, Object, Object)}.
     */
    @SuppressWarnings("OverrideOnly")
    @ApiStatus.Experimental
    public final void openActive(
            @NotNull Class<? extends View> viewClass,
            @NotNull String contextId,
            @NotNull TowncraftPlayer player,
            Object initialData) {
        internalOpenActiveContext(viewClass, contextId, player, initialData);
    }

    /**
     * Opens an already active context to a player.
     * <p>
     * <b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param endlessContextInfo The id of the context.
     * @param player             Who the context will be open to.
     */
    @ApiStatus.Experimental
    public final void openEndless(@NotNull EndlessContextInfo endlessContextInfo, @NotNull TowncraftPlayer player) {
        openEndless(endlessContextInfo, player, null);
    }
    // endregion

    /**
     * Opens an already active context to a player.
     * <p>
     * <b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param endlessContextInfo The id of the context.
     * @param player             Who the context will be open to.
     * @param initialData        Initial data to pass to {@link PlatformView#onViewerAdded(IFContext, Object, Object)}.
     */
    @SuppressWarnings({"unchecked", "OverrideOnly"})
    @ApiStatus.Experimental
    public final void openEndless(
            @NotNull EndlessContextInfo endlessContextInfo, @NotNull TowncraftPlayer player, Object initialData) {
        openActive(
                (Class<? extends View>) endlessContextInfo.getView().getClass(),
                endlessContextInfo.getContextId(),
                player,
                initialData);
    }

    @Override
    public final ViewFrame register() {
        if (isRegistered()) throw new IllegalStateException("This view frame is already registered");

        PlatformUtils.setFactory(new TowncraftElementFactory());
        setRegistered(true);
        getPipeline().execute(IFViewFrame.FRAME_REGISTERED, this);
        initializeViews();
        return this;
    }

    @Override
    public final void unregister() {
        if (!isRegistered()) return;

        // Locks new operations while unregistering
        setRegistered(false);

        final Iterator<View> iterator = registeredViews.values().iterator();
        while (iterator.hasNext()) {
            final View view = iterator.next();
            try {
                view.closeForEveryone();
            } catch (final RuntimeException ignored) {
            }
            iterator.remove();
        }
        getPipeline().execute(IFViewFrame.FRAME_UNREGISTERED, this);
    }

    private void initializeViews() {
        for (final Map.Entry<UUID, View> entry : getRegisteredViews().entrySet()) {
            final View view = entry.getValue();

            try {
                view.internalInitialization(this);
                view.setInitialized(true);
            } catch (final Throwable exception) {
                view.setInitialized(false);
                Towncraft.getLogger().error("An error occurred while enabling view {}", view.getClass().getName());
                Towncraft.getLogger().error("ERROR: ", exception);
            }
        }
    }
    // endregion

    /**
     * <b><i> This is an internal inventory-framework API that should not be used from outside of
     * this library. No compatibility guarantees are provided. </i></b>
     */
    @ApiStatus.Internal
    public final Viewer getViewer(@NotNull TowncraftPlayer player) {
        return viewerById.get(player.getUUID().toString());
    }

    /**
     * Installs a feature.
     *
     * @param feature   The feature to be installed.
     * @param configure The feature configuration.
     * @param <C>       The feature configuration type.
     * @param <R>       The feature value instance type.
     * @return An instance of the installed feature.
     */
    public final <C, R> ViewFrame install(
            @NotNull Feature<C, R, ViewFrame> feature, @NotNull UnaryOperator<C> configure) {
        featureInstaller.install(feature, configure);
        IFDebug.debug("Feature %s installed", feature.name());
        return this;
    }

    /**
     * Installs a feature with no specific configuration.
     *
     * @param feature The feature to be installed.
     * @return This view frame.
     */
    @NotNull
    public final ViewFrame install(@NotNull Feature<?, ?, ViewFrame> feature) {
        return install(feature, UnaryOperator.identity());
    }
}
