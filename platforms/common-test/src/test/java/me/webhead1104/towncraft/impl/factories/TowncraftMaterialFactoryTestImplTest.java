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
package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import me.webhead1104.towncraft.impl.items.TowncraftMaterialTestImpl;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;

class TowncraftMaterialFactoryTestImplTest {
    private static final Key KEY = key("stone");
    private final TowncraftMaterialFactoryTestImpl factory = new TowncraftMaterialFactoryTestImpl();

    @Test
    void testGet0() {
        TowncraftMaterial result = factory.get0(KEY);
        Assertions.assertEquals(new TowncraftMaterialTestImpl(KEY), result);
    }

    @Test
    void testGet_throughStaticMethod() {
        TowncraftMaterial result = TowncraftMaterialFactory.get(KEY);
        Assertions.assertEquals(new TowncraftMaterialTestImpl(KEY), result);
    }
}