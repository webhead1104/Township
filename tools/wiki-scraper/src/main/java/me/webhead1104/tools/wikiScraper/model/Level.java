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