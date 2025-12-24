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
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.ClickType;

@Getter
public class TowncraftInventoryClickEvent extends TowncraftInventoryInteractEvent {
    private final ClickType clickType;
    private final int slot;
    private final TowncraftItemStack clickedItem;
    private final int hotbarKey;
    private final TowncraftInventory clickedInventory;

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, int slot, ClickType click, int hotbarKey, TowncraftItemStack clickedItem, TowncraftInventory clickedInventory) {
        super(view);
        this.slot = slot;
        this.clickType = click;
        this.hotbarKey = hotbarKey;
        this.clickedItem = clickedItem;
        this.clickedInventory = clickedInventory;
    }
}
