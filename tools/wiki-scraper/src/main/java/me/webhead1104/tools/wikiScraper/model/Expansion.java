package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Setter
@Getter
@ConfigSerializable
@AllArgsConstructor
public class Expansion {
    @Setting("population_needed")
    private int populationNeeded;
    @Setting("coins_needed")
    private int coinsNeeded;
    private String time;
    private int xp;
    @Setting("tools_needed")
    private Tools toolsNeeded;

    public Expansion(Elements row, int previousXp) {
        this.populationNeeded = Integer.parseInt(row.get(1).text().replace(" ", ""));
        this.coinsNeeded = Integer.parseInt(row.get(3).text().replace(" ", ""));
        this.time = row.get(4).text();
        this.toolsNeeded = new Tools(row.get(2));
        String xpString = row.get(5).text().replace(" ", "");
        if (xpString.isEmpty()) {
            this.xp = previousXp + 2;
            return;
        }
        this.xp = Integer.parseInt(xpString);
    }

    @Setter
    @Getter
    @ConfigSerializable
    @AllArgsConstructor
    public static class Tools {
        private int axe;
        private int shovel;
        private int saw;

        public Tools(Element cell) {
            String[] tools = cell.text().split(" ");
            this.axe = Integer.parseInt(tools[0]);
            this.shovel = Integer.parseInt(tools[1]);
            this.saw = Integer.parseInt(tools[2]);
        }
    }
}
