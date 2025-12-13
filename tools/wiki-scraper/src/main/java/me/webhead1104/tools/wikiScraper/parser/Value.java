package me.webhead1104.tools.wikiScraper.parser;

public record Value(String text) {

    public String getAsKey() {
        return text.replaceAll(" ", "_").replaceAll("_x3", "").replaceAll("'", "").toLowerCase();
    }

    public int getAsInt() throws NumberFormatException {
        return Integer.parseInt(text);
    }

    public int getAsCoins() throws NumberFormatException {
        if (text.equals("Free")) {
            return 0;
        }
        return Integer.parseInt(text.replaceAll("N/A", "0").replaceAll("\\D+", ""));
    }

    public int getAsLevel() throws NumberFormatException {
        return Integer.parseInt(text.replaceAll("\\D+", ""));
    }
}
