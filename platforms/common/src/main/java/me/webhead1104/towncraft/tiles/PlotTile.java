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

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.plots.PlotMenu;
import me.webhead1104.towncraft.features.world.plots.PlotType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlotTile extends BuildingTile implements TimeFinishable {
    private Key plotType = Towncraft.NONE_KEY;
    private @Nullable Instant instant;
    private boolean claimable = false;

    @Keep
    public PlotTile(@NonNull Key plotType, @Nullable Instant instant, boolean claimable) {
        super(Towncraft.key("plot"));
        this.plotType = plotType;
        this.instant = instant;
        this.claimable = claimable;
    }

    @Keep
    public PlotTile() {
        super(Towncraft.key("plot"));
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = Towncraft.getDataLoader(PlotType.class).get(plotType).getMenuItem();
        if (plotType.equals(Towncraft.NONE_KEY) || instant == null) {
            return itemStack;
        }

        itemStack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), instant)));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        if (plotType.equals(Towncraft.NONE_KEY)) {
            Map<String, Object> map = new HashMap<>(Map.of(
                    "slot", slot,
                    "section", worldSection.getSection()
            ));
            context.openForPlayer(PlotMenu.class, map);
            return true;
        } else if (claimable) {
            this.claimable = false;
            context.getUser().getBarn().addAmountToItem(Towncraft.getDataLoader(PlotType.class).get(plotType).getItem().key(), 1);
            this.plotType = Towncraft.NONE_KEY;
        }
        return false;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        this.claimable = true;
    }
}
