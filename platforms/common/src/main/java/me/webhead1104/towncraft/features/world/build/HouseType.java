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
package me.webhead1104.towncraft.features.world.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import net.kyori.adventure.key.Key;

@Getter
@AllArgsConstructor
public enum HouseType {
    COTTAGE("Cottage", Towncraft.key("cottage")),
    CAPE_COD_COTTAGE("Cape Cod Cottage", Towncraft.key("cape_cod_cottage")),
    CHALET_BUNGALOW("Chalet Bungalow", Towncraft.key("chalet_bungalow")),
    CONCH_HOUSE("Conch House", Towncraft.key("conch_house")),
    BUNGALOW("Bungalow", Towncraft.key("bungalow")),
    FARMHOUSE("Farmhouse", Towncraft.key("farmhouse"));
    private final String name;
    private final Key buildingType;
}
