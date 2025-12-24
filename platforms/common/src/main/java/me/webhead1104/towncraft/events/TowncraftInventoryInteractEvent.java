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
package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class TowncraftInventoryInteractEvent extends TowncraftInventoryEvent implements TowncraftCancellable {
    private Event.Result result = Event.Result.DEFAULT;

    public TowncraftInventoryInteractEvent(@NotNull TowncraftInventoryView view) {
        super(view);
    }

    public TowncraftPlayer getPlayer() {
        return this.getView().getPlayer();
    }

    public void setResult(@NotNull Result newResult) {
        this.result = newResult;
    }

    @Override
    public boolean isCancelled() {
        return this.getResult() == Result.DENY;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.setResult(cancel ? Result.DENY : Result.ALLOW);
    }
}
