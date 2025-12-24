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
package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.select.Elements;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Setter
@Getter
@ConfigSerializable
@AllArgsConstructor
public class Level {
    @Setting("xp_needed")
    private int xpNeeded;
    @Setting("coins_given")
    private int coinsGiven;
    @Setting("cash_given")
    private int cashGiven;

    public Level(Elements elements, int row) {
        String[] textElements = elements.text().split(" ");

        if (row < 8) {
            this.xpNeeded = Integer.parseInt(textElements[1].replace("xp", ""));
            this.coinsGiven = Integer.parseInt(textElements[2]);
            this.cashGiven = Integer.parseInt(textElements[3]);
        } else if (row <= 65) {
            this.xpNeeded = Integer.parseInt(textElements[1].split("xp")[0].replace(",", ""));

            for (int i = 2; i < textElements.length; i++) {
                if (!textElements[i].contains("C")) {
                    this.coinsGiven = Integer.parseInt(textElements[i].replace(",", ""));
                    break;
                }
            }
        } else {
            this.xpNeeded = Integer.parseInt(textElements[1].replace("xp", "").replace(",", ""));

            for (int i = 2; i < textElements.length; i++) {
                if (!textElements[i].contains("C")) {
                    if (textElements[i].equals("xp")) {
                        this.cashGiven = Integer.parseInt(textElements[i - 1].replace(",", ""));
                        break;
                    }
                    this.cashGiven = Integer.parseInt(textElements[i].replace(",", ""));
                    break;
                }
            }
        }
    }
}