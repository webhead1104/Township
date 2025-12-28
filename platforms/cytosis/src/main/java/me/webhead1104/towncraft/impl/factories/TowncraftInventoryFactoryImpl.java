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
package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftInventoryFactory;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class TowncraftInventoryFactoryImpl implements TowncraftInventoryFactory {
    @Override
    public TowncraftInventory create(TowncraftInventoryType type, Component title) {
        return new TowncraftInventoryImpl(new Inventory(getInventoryType(type), title));
    }

    @Override
    public TowncraftInventory create(int size, Component title) {
        return new TowncraftInventoryImpl(new Inventory(getInventoryType(size), title));
    }

    private InventoryType getInventoryType(TowncraftInventoryType inventoryType) {
        if (inventoryType == TowncraftInventoryType.CHEST) {
            return InventoryType.CHEST_6_ROW;
        }
        return InventoryType.valueOf(inventoryType.name());
    }

    private InventoryType getInventoryType(int size) {
        return switch (size / 9) {
            case 1 -> InventoryType.CHEST_1_ROW;
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            default -> InventoryType.CHEST_6_ROW;
        };
    }
}
