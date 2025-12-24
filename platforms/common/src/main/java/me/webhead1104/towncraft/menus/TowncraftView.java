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
