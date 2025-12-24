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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.expansions.ExpansionMenu;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionTile extends Tile implements TimeFinishable {
    private @Nullable Instant instant;

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (instant == null) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PODZOL);
            itemStack.setName(Msg.format("Expansion"));
            return itemStack;
        }

        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.DIRT);
        itemStack.setName(Msg.format("Expansion"));
        itemStack.setLore(Msg.format("<gold>Time<white>: %s", Utils.format(Instant.now(), instant)));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        if (instant != null) return false;
        int row = context.getClickedSlot() / 9;
        int col = context.getClickedSlot() % 9;

        int topLeftRow = row - (row % 3);
        int topLeftCol = col - (col % 3);

        context.openForPlayer(ExpansionMenu.class, topLeftRow * 9 + topLeftCol);
        return true;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        worldSection.setSlot(slot, StaticWorldTile.Type.GRASS.getTile());
    }
}
