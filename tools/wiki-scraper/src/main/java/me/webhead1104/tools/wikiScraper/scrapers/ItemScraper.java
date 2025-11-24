package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Slf4j
@Keep
public class ItemScraper implements Scraper<Item> {
    public static final Map<String, String> ITEM_NAMES = new HashMap<>();
    private static final String BASE_URL = "https://township.fandom.com/wiki/Goods";
    private static final TypeToken<Map<String, String>> TOKEN = new TypeToken<>() {
    };
    private static final List<Item> feedMillItems = new ArrayList<>();

    @Override
    public String id() {
        return "item";
    }

    @Override
    public List<Item> scrape() throws IOException {
        InputStream stream = Objects.requireNonNull(getClass().getResourceAsStream("/data/item_names.json"));
        ITEM_NAMES.putAll(new Gson().fromJson(new String(stream.readAllBytes()), TOKEN));
        stream.close();

        Document doc = fetchPage(BASE_URL);

        Elements tables = doc.select("div.mw-collapsible");
        log.debug("Found {} tables on the page", tables.size());
        List<Item> items = new ArrayList<>();
        items.add(new Item("none", "minecraft:barrier", -1, -1));
        for (Element table : tables) {
            String toMatch = table.select("tr").getFirst().text();
            log.debug("MATCHING '{}' FOUND {}", toMatch, Type.match(toMatch));
            Type type = Type.match(toMatch);
            List<Element> cols = table.select("tr");
            if (type == Type.UNKNOWN) {
                log.warn("Skipping '{}' as it doesn't match anything", toMatch);
                for (Element col : cols) {
                    log.debug("FOUND {}", col.text());
                }
                continue;
            }
            List<Element> rows = new ArrayList<>(cols.subList(type.getStartingRow(), cols.size()));
            log.debug("Type = {}", type);
            items.addAll(type.parse(rows));
        }
        items.removeIf(item -> item.getLevelNeeded() > Main.MAX_LEVEL);
        return items;
    }

    @Override
    public File outputPath() {
        return new File("items.json");
    }

    @Getter
    enum Type {
        UNKNOWN("", 0, 0, 0) {
            @Override
            public List<Item> parse(List<Element> rows) {
                throw new UnsupportedOperationException("Tried to parse UNKNOWN type");
            }
        },
        FARM("Click on link to go down to Feed Mill", 4, 3, 4, i -> i < 11 || i > 14) {
            @Override
            public List<Item> parse(List<Element> rows) {
                List<Item> items = super.parse(rows);
                feedMillItems.addAll(items.subList(11, items.size()));
                return items.subList(0, 11);
            }
        },
        CROPS("Crops", 2, 2, 5, i -> i < 24),
        FACTORY("Asian ~ .*", 3, 3, 4) {
            @Override
            public List<Item> parse(List<Element> rows) {
                List<Item> items = feedMillItems;
                items.addAll(super.parse(rows));
                return items;
            }
        },
        ISLAND("Islands and Ships", 3, 2, 3) {
            private int lastLevel = 0;

            @Override
            public List<Item> parse(List<Element> rows) {
                List<Item> items = new ArrayList<>();
                for (Element row : rows) {
                    Elements cells = row.select("td");
                    if (cells.size() == 8) {
                        Item item = parseRow(cells, 1);
                        items.add(item);
                        lastLevel = item.getLevelNeeded();
                        continue;
                    }
                    String key = cells.getFirst().text();
                    int coinsNeeded = Integer.parseInt(cells.get(2).text().replaceAll("\\s+coins?\\b", "").replaceAll("N/A", "0"));
                    items.add(new Item(
                            key,
                            lastLevel,
                            coinsNeeded
                    ));
                }
                return items;
            }
        },
        MINE("Mine", 1, 2, 3, i -> i < 9 || i > 11) {
            @Override
            public Item parseRow(Elements cells, int i) {
                if (11 > i) {
                    return super.parseRow(cells, i);
                }
                String key = cells.getFirst().text();
                int levelNeeded = Integer.parseInt(cells.get(2).text().replaceAll("\\D", ""));
                int coinsNeeded = Integer.parseInt(cells.get(4).text().replaceAll("\\s+coins?\\b", "").replaceAll("N/A", "0"));
                return new Item(
                        key,
                        levelNeeded,
                        coinsNeeded
                );
            }
        },
        CONSTRUCTION("Building Materials Barn Tools", 2, -1, -1) {
            @Override
            public List<Item> parse(List<Element> rows) {
                List<Item> items = new ArrayList<>();
                List<Item> secondCol = new ArrayList<>();
                for (Element row : rows) {
                    Elements cells = row.select("td");
                    if (cells.size() != 7) {
                        if (cells.text().equals("TowncraftMaterial Sell Price  TowncraftMaterial Sell Price")) {
                            items.addAll(secondCol);
                            secondCol.clear();
                        }
                        continue;
                    }

                    items.add(new Item(
                            cells.getFirst().text(),
                            0,
                            Integer.parseInt(cells.get(2).text().replaceAll("\\s+coins?\\b", "").replaceAll("N/A", "0"))
                    ));

                    secondCol.add(new Item(
                                    cells.get(4).text(),
                                    0,
                                    Integer.parseInt(cells.get(6).text().replaceAll("\\s+coins?\\b", "").replaceAll("N/A", "0"))
                            )
                    );
                }
                items.addAll(secondCol);
                return items;
            }
        };
        private final Pattern pattern;
        private final int startingRow;
        private final int levelCell;
        private final int coinCell;
        private final Predicate<Integer> runOnThisRow;

        Type(String pattern, int startingRow, int levelCell, int coinCell) {
            this(pattern, startingRow, levelCell, coinCell, obj -> true);
        }

        Type(String pattern, int startingRow, int levelCell, int coinCell, Predicate<Integer> runOnThisRow) {
            this.pattern = Pattern.compile(pattern);
            this.startingRow = startingRow;
            this.levelCell = levelCell;
            this.coinCell = coinCell;
            this.runOnThisRow = runOnThisRow.negate();
        }

        public static Type match(String pattern) {
            for (Type type : Type.values()) {
                if (type.pattern.matcher(pattern).matches()) {
                    return type;
                }
            }

            return UNKNOWN;
        }

        public List<Item> parse(List<Element> rows) {
            int i = 0;
            List<Item> items = new ArrayList<>();
            for (Element row : rows) {
                int neededSize = switch (this) {
                    case ISLAND -> 8;
                    case MINE -> 7;
                    default -> 9;
                };
                if (row.select("td").size() < neededSize || runOnThisRow.test(i)) {
                    i++;
                    continue;
                }
                items.add(parseRow(row.select("td"), i));
                i++;
            }
            return items;
        }

        public Item parseRow(Elements cells, int i) {
            String key = cells.getFirst().text();
            int levelNeeded = Integer.parseInt(cells.get(levelCell).text().replaceAll("\\D", ""));
            int coinsNeeded = Integer.parseInt(cells.get(coinCell).text().replaceAll("\\s+coins?\\b", "").replaceAll("N/A", "0"));
            return new Item(
                    key,
                    levelNeeded,
                    coinsNeeded
            );
        }
    }
}
