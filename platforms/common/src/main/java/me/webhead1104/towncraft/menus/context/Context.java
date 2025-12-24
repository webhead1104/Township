/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.menus.context;

import me.devnatan.inventoryframework.context.IFConfinedContext;
import me.devnatan.inventoryframework.context.IFOpenContext;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public interface Context extends IFConfinedContext {

    /**
     * The player for the current interaction context.
     * <p>
     * Contexts can be shared and contain multiple viewers, this method will
     * always return the player for the current event.
     *
     * @return A player in this interaction context.
     */
    @UnknownNullability
    TowncraftPlayer getPlayer();

    @NotNull
    User getUser();

    /**
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     */
    @ApiStatus.Experimental
    List<TowncraftPlayer> getAllPlayers();

    /**
     * Updates the container title for a specific player.
     *
     * <p>This should not be used before the container is opened, if you need to set the __initial
     * title__ use {@link IFOpenContext#modifyConfig()} on open handler instead.
     *
     * <p>This method is version dependant, so it may be that your server version is not yet
     * supported, if you try to use this method and fail (can fail silently), report it to the
     * library developers to add support to your version.
     *
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param title  The new container title.
     * @param player The player to update the title.
     */
    @ApiStatus.Experimental
    void updateTitleForPlayer(@NotNull String title, @NotNull TowncraftPlayer player);

    /**
     * Resets the container title only for the player current scope of execution to the initially
     * defined title. Must be used after {@link #updateTitleForPlayer(String, TowncraftPlayer)} to take effect.
     *
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     *
     * @param player The player to reset the title.
     */
    @ApiStatus.Experimental
    void resetTitleForPlayer(@NotNull TowncraftPlayer player);
}
