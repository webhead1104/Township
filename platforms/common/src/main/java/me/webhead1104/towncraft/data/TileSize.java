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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public record TileSize(int height, int width) {
    public static final TileSize SIZE_1X1 = new TileSize(1, 1);
    public static final TileSize SIZE_2X2 = new TileSize(2, 2);
    public static final TileSize SIZE_3X3 = new TileSize(3, 3);
    public static final TileSize SIZE_2X1 = new TileSize(1, 2);
    public static final TileSize SIZE_1X3 = new TileSize(1, 3);

    public List<Integer> toList(int startAt) {
        List<Integer> area = new ArrayList<>();
        int currentValue = startAt;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                area.add(currentValue++);
            }
            currentValue += 9 - width;
        }
        return area;
    }
}
