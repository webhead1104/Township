/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
import lombok.Setter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ConstructionTile extends Tile implements TimeFinishable {
    private String name;
    private Instant instant;
    private Tile tile;

    public ConstructionTile(String name, Duration duration, Tile tile) {
        this.name = name;
        this.instant = Instant.now().plus(duration);
        this.tile = tile;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (instant == null) {
            handleUpdate(context, worldSection, slot);
            return null;
        }
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(name));
        itemStack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), instant)));
        return itemStack;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        worldSection.setSlot(slot, tile);
    }
}
