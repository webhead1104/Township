package me.webhead1104.tools.wikiScraper.parser;

import me.webhead1104.tools.wikiScraper.core.Utils;

public record Value(String text) {

    public String getAsKey() {
        return Utils.normalizeForKey(text);
    }

    public int getAsCoins() throws NumberFormatException {
        if (text.equals("Free")) {
            return 0;
        }
        if (text.equals("-")) return 0;
        return Integer.parseInt(text.replaceAll("N/A", "0").replaceAll("\\D+", ""));
    }

    public int getAsLevel() throws NumberFormatException {
        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }

    public int getAsXp() throws NumberFormatException {
        if (text.isEmpty()) return 0;
        if (text.equals("-")) return 0;
        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }

    public int getAsPopulation() throws NumberFormatException {
        if (text.isEmpty()) return 0;
        return Integer.parseInt(text.replaceAll("[Nn]/[Aa]", "0").replaceAll("\\D+", ""));
    }
}
