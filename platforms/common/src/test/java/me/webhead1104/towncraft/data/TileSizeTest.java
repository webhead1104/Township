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
package me.webhead1104.towncraft.data;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileSizeTest {

    @Test
    public void testToList1x1() {
        TileSize size = TileSize.SIZE_1X1;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0), area);

        area = size.toList(10);
        assertEquals(List.of(10), area);
    }

    @Test
    public void testToList2x2() {
        TileSize size = TileSize.SIZE_2X2;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 9, 10), area);
    }

    @Test
    public void testToList2x1() {
        TileSize size = TileSize.SIZE_2X1;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1), area);
    }

    @Test
    public void testToList1x3() {
        TileSize size = TileSize.SIZE_1X3;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 2), area);
    }

    @Test
    public void testToList3x3() {
        TileSize size = TileSize.SIZE_3X3;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 2, 9, 10, 11, 18, 19, 20), area);
    }
}
