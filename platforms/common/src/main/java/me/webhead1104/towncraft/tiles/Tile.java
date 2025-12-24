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
package me.webhead1104.towncraft.tiles;

import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import org.jetbrains.annotations.ApiStatus;

public abstract class Tile {
    public abstract TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot);

    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        return false;
    }

    @ApiStatus.OverrideOnly
    public void onUpdate(SlotContext slotContext, WorldSection worldSection, int slot) {
        if (this instanceof TimeFinishable timeFinishable) {
            timeFinishable.handleUpdate(slotContext, worldSection, slot);
        }
    }

    @ApiStatus.OverrideOnly
    public void onLoad(RenderContext renderContext, WorldSection worldSection, int slot) {
        if (this instanceof TimeFinishable timeFinishable) {
            timeFinishable.handleUpdate(renderContext, worldSection, slot);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}