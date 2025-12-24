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

import lombok.AllArgsConstructor;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.ItemRarity;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.Sets;

import java.util.List;

@AllArgsConstructor
public class TowncraftItemStackImpl implements TowncraftItemStack {
    private ItemStack itemStack;

    @Override
    public void setName(Component component) {
        itemStack = itemStack.with(DataComponents.ITEM_NAME, component);
    }

    @Override
    public void setLore(List<Component> lore) {
        itemStack = itemStack.with(DataComponents.LORE, lore);
    }

    @Override
    public void overrideNameColor() {
        itemStack = itemStack.with(DataComponents.RARITY, ItemRarity.COMMON);
    }

    @Override
    public boolean isSimilar(@NotNull TowncraftItemStack other) {
        return itemStack.isSimilar((ItemStack) other.toPlatform());
    }

    @Override
    public TowncraftMaterial getMaterial() {
        return new TowncraftMaterialImpl(itemStack.material());
    }

    @Override
    public void setMaterial(TowncraftMaterial material) {
        itemStack = itemStack.withMaterial(((TowncraftMaterialImpl) material).material());
    }

    @Override
    public void setPlayerHeadTexture(String texture) {
        ResolvableProfile profile = new ResolvableProfile(new ResolvableProfile.Partial(null, null, List.of(new GameProfile.Property("textures", texture))));
        itemStack = itemStack.with(DataComponents.PROFILE, profile);
    }

    @Override
    public void hideTooltip(boolean hideTooltip) {
        itemStack = itemStack.with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(hideTooltip, Sets.newHashSet()));
    }

    @Override
    public boolean isEmpty() {
        return itemStack.equals(ItemStack.AIR) || itemStack.material() == Material.AIR || itemStack.amount() <= 0;
    }

    @Override
    public Object toPlatform() {
        return itemStack;
    }
}
