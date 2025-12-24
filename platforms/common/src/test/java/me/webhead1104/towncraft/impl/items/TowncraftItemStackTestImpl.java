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

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TowncraftItemStackTestImpl implements TowncraftItemStack {
    private TowncraftMaterial material;
    private Component name;
    private List<Component> lore = new ArrayList<>();
    private String playerHeadTexture;

    public TowncraftItemStackTestImpl(TowncraftMaterial material) {
        this.material = material;
        this.name = Msg.format(Utils.thing2(material.getKey().value()));
    }

    @Override
    public void setName(Component component) {
        this.name = component;
    }

    @Override
    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    @Override
    public void overrideNameColor() {
    }

    @Override
    public boolean isSimilar(@NotNull TowncraftItemStack other) {
        return false;
    }

    @Override
    public TowncraftMaterial getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(TowncraftMaterial material) {
        this.material = material;
    }

    @Override
    public void setPlayerHeadTexture(String texture) {
    }

    @Override
    public void hideTooltip(boolean hideTooltip) {
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Object toPlatform() {
        return null;
    }
}
