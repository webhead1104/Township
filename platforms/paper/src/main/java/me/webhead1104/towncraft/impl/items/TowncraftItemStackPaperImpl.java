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
package me.webhead1104.towncraft.impl.items;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import lombok.AllArgsConstructor;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class TowncraftItemStackPaperImpl implements TowncraftItemStack {
    private ItemStack itemStack;

    @Override
    public void setName(Component component) {
        itemStack.setData(DataComponentTypes.ITEM_NAME, component);
    }

    @Override
    public void setLore(List<Component> lore) {
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
    }

    @Override
    public void overrideNameColor() {
        itemStack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
    }

    @Override
    public boolean isSimilar(@NotNull TowncraftItemStack other) {
        return itemStack.isSimilar((ItemStack) other.toPlatform());
    }

    @Override
    public TowncraftMaterial getMaterial() {
        return new TowncraftMaterialPaperImpl(itemStack.getType());
    }

    @Override
    public void setMaterial(TowncraftMaterial material) {
        itemStack = itemStack.withType(((TowncraftMaterialPaperImpl) material).material());
    }

    @Override
    public void setPlayerHeadTexture(String texture) {
        ResolvableProfile profile = ResolvableProfile.resolvableProfile().addProperty(new ProfileProperty("textures", texture)).build();
        itemStack.setData(DataComponentTypes.PROFILE, profile);
    }

    @Override
    public void hideTooltip(boolean hideTooltip) {
        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(hideTooltip).build());
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    @Override
    public Object toPlatform() {
        return itemStack;
    }
}
