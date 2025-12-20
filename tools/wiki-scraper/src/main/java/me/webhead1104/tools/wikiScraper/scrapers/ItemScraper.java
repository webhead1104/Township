package me.webhead1104.tools.wikiScraper.scrapers;

import com.google.errorprone.annotations.Keep;
import io.leangen.geantyref.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.cli.Main;
import me.webhead1104.tools.wikiScraper.core.Scraper;
import me.webhead1104.tools.wikiScraper.core.Utils;
import me.webhead1104.tools.wikiScraper.model.Item;
import me.webhead1104.tools.wikiScraper.parser.Page;
import me.webhead1104.tools.wikiScraper.parser.Row;
import me.webhead1104.tools.wikiScraper.parser.Table;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Slf4j
@Keep
public class ItemScraper implements Scraper<Item> {
    public static final Map<String, String> ITEM_NAMES = new HashMap<>();
    private static final TypeToken<Map<String, String>> TOKEN = new TypeToken<>() {
    };
    private static final String BASE_URL = "https://township.fandom.com/wiki/Goods";
    private static final List<Item> FEED_MILL_ITEMS = new ArrayList<>();

    static {
        try {
            ITEM_NAMES.putAll(Utils.readJson("/data/item_names.json", TOKEN));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String id() {
        return "item";
    }

    @Override
    public List<Item> scrape() throws IOException {
        List<Item> items = new ArrayList<>();
        items.add(new Item("none", "minecraft:barrier", -1, -1));

        List<Table> tables = new Page(fetchPage(BASE_URL), "div.mw-collapsible").getTables();

        for (Table table : tables) {
            Type type = Type.match(table.getFirstRow().getText());
            if (type == Type.UNKNOWN) {
                log.warn("Skipping '{}' as it doesn't match anything", table.getFirstRow().getText());
                continue;
            }
            log.debug("Type = {}", type);
            items.addAll(type.parse(table.getRows()));
        }
        items.removeIf(item -> item.getLevelNeeded() > Main.MAX_LEVEL);
        return items;
    }

    @Override
    public Class<Item> resultType() {
        return Item.class;
    }

    @Override
    public File outputPath() {
        return new File("items.json");
    }

    @Getter
    enum Type {
        UNKNOWN("", -1, -1, -1, -1, -1, i -> true),
        FARM("Click on link to go down to Feed Mill", 4, 0, 3, 4, 9, i -> i > 10 && i < 15) {
            private String name;

            public List<Item> parse(List<Row> rows) {
                rows = rows.subList(4, rows.size());
                List<Item> items = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    Row row = rows.get(i);
                    if (i == 12) {
                        name = row.getText();
                        continue;
                    }
                    if (i > 10 && i < 15) {
                        continue;
                    }

                    if (row.getValues().size() != 9) {
                        continue;
                    }

                    String key = row.getValue(0).getAsKey();
                    String material = ItemScraper.ITEM_NAMES.getOrDefault(key, "minecraft:player_head");
                    int level = row.getValue(3).getAsLevel();
                    int coins = row.getValue(4).getAsCoins();
                    if (i > 14) {
                        int xpGiven = row.getValue(5).getAsXp();
                        FEED_MILL_ITEMS.add(new Item(
                                key,
                                material,
                                level,
                                coins,
                                new Item.FactoryData(
                                        name,
                                        row.getValue(0).text(),
                                        parseIngredients(row),
                                        xpGiven,
                                        row.getValue(6).text()
                                )
                        ));
                        continue;
                    }
                    items.add(new Item(
                            key,
                            material,
                            level,
                            coins
                    ));
                }
                return items.subList(0, 11);
            }
        },
        CROPS("Crops", 2, 0, 2, 5, 9, i -> i > 24),
        FACTORY("Asian ~ .*", 3, 0, 3, 4, 9, i -> i > 373, items -> {
            items.addAll(0, FEED_MILL_ITEMS);
            return items;
        }) {
            private String lastName;

            public List<Item> parse(List<Row> rows) {
                rows = rows.subList(1, rows.size());
                List<Item> items = new ArrayList<>(FEED_MILL_ITEMS);
                for (int i = 0; i < rows.size(); i++) {
                    Row row = rows.get(i);
                    if (i > 373) {
                        continue;
                    }

                    if (row.getValues().size() == 1) {
                        lastName = row.getText();
                        continue;
                    }

                    if (row.getValues().size() != 9) {
                        continue;
                    }

                    String key = row.getValue(0).getAsKey();
                    String material = ItemScraper.ITEM_NAMES.getOrDefault(key, "minecraft:player_head");
                    int level = row.getValue(3).getAsLevel();
                    int coins = row.getValue(4).getAsCoins();
                    int xpGiven = row.getValue(5).getAsXp();
                    items.add(new Item(
                            key,
                            material,
                            level,
                            coins,
                            new Item.FactoryData(
                                    lastName,
                                    row.getValue(0).text(),
                                    parseIngredients(row),
                                    xpGiven,
                                    row.getValue(6).text()
                            )
                    ));
                }

                return items;
            }
        },
        ISLAND("Islands and Ships", -1, -1, -1, -1, -1, i -> false) {
            private int lastLevel = 0;

            @Override
            public List<Item> parse(List<Row> rows) {
                rows = rows.subList(3, rows.size());
                List<Item> items = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    Row row = rows.get(i);
                    if (i > 18) {
                        return items;
                    }

                    if (row.getValues().size() != 8 && row.getValues().size() != 6) {
                        continue;
                    }

                    String key = row.getValue(0).getAsKey();
                    if (row.getValues().size() == 8) {
                        lastLevel = row.getValue(2).getAsLevel();
                    }
                    items.add(new Item(
                            key,
                            ItemScraper.ITEM_NAMES.getOrDefault(key, "minecraft:player_head"),
                            lastLevel,
                            row.getValue(row.getValues().size() == 8 ? 3 : 2).getAsCoins()
                    ));
                }
                return items;
            }
        },
        MINE("Mine", 2, 0, 2, 3, 7, i -> false) {
            @Override
            public List<Item> parse(List<Row> rows) {
                rows = rows.subList(2, rows.size());
                List<Item> items = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    Row row = rows.get(i);

                    if (row.getValues().size() != 7) {
                        continue;
                    }

                    String key = row.getValue(0).getAsKey();
                    items.add(new Item(
                            key,
                            ItemScraper.ITEM_NAMES.getOrDefault(key, "minecraft:player_head"),
                            row.getValue(2).getAsLevel(),
                            row.getValue(i > 10 ? 4 : 3).getAsCoins()
                    ));
                }
                return items;
            }
        },
        CONSTRUCTION("Building Materials Barn Tools", -1, -1, -1, -1, -1, i -> false) {
            @Override
            public List<Item> parse(List<Row> rows) {
                rows = rows.subList(0, rows.size());
                List<Item> items = new ArrayList<>();
                List<Item> otherItems = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    if (otherItems.size() == 3) {
                        items.addAll(otherItems);
                        otherItems.clear();
                    }
                    Row row = rows.get(i);
                    if (i >= 10) {
                        return items;
                    }

                    if (row.getValues().size() != 7) {
                        continue;
                    }

                    items.add(new Item(
                            row.getValue(0).getAsKey(),
                            ItemScraper.ITEM_NAMES.getOrDefault(row.getValue(0).getAsKey(), "minecraft:player_head"),
                            0,
                            row.getValue(2).getAsCoins(),
                            8    // Very rare (special construction items)
                    ));

                    otherItems.add(new Item(
                            row.getValue(4).getAsKey(),
                            ItemScraper.ITEM_NAMES.getOrDefault(row.getValue(4).getAsKey(), "minecraft:player_head"),
                            0,
                            row.getValue(6).getAsCoins(),
                            8    // Very rare (special construction items)
                    ));
                }
                return items;
            }
        };
        private final Pattern pattern;
        private final int startingRow;
        private final int keyCell;
        private final int levelCell;
        private final int coinCell;
        private final int neededSize;
        private final Predicate<Integer> noRun;
        private final UnaryOperator<List<Item>> runAfterAll;

        Type(String pattern, int startingRow, int keyCell, int levelCell, int coinCell, int neededSize,
             Predicate<Integer> noRun) {
            this(pattern, startingRow, keyCell, levelCell, coinCell, neededSize, noRun, items -> items);
        }

        Type(String pattern, int startingRow, int keyCell, int levelCell, int coinCell, int neededSize,
             Predicate<Integer> noRun, UnaryOperator<List<Item>> runAfterAll) {
            this.pattern = Pattern.compile(pattern);
            this.startingRow = startingRow;
            this.keyCell = keyCell;
            this.levelCell = levelCell;
            this.coinCell = coinCell;
            this.neededSize = neededSize;
            this.noRun = noRun;
            this.runAfterAll = runAfterAll;
        }

        public static Type match(String pattern) {
            for (Type type : Type.values()) {
                if (type.pattern.matcher(pattern).matches()) {
                    return type;
                }
            }

            return UNKNOWN;
        }

        public final Map<String, Integer> parseIngredients(Row row) {
            Map<String, Integer> ingredients = new LinkedHashMap<>();
            String html = row.getElement().select("th, td").stream().map(Element::wholeText).toList().get(2);
            for (String ingredient : html.split("\n")) {
                ingredient = ingredient.trim();
                if (ingredient.isEmpty()) continue; // Skip empty lines

                String[] values = ingredient.split(" ", 2); // Split into at most 2 parts

                if (values.length == 2) {
                    String quantityStr = values[0];
                    String name = values[1];
                    ingredients.put(Utils.normalizeForKey(name), Integer.valueOf(quantityStr));
                } else {
                    log.warn("Could not parse ingredient: {}", ingredient);
                }
            }
            return ingredients;
        }

        public List<Item> parse(List<Row> rows) {
            rows = rows.subList(startingRow, rows.size());
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                Row row = rows.get(i);
                log.debug("Text = {} for type {} i = {}", row.getText(), name(), i);
                if (noRun.test(i)) {
                    log.debug("NoRun = {}", i);
                    continue;
                }

                if (row.getValues().size() != neededSize) {
                    log.debug("Not enough values for row {} size = {}", i, row.getValues().size());
                    continue;
                }

                log.debug("RUNNING Text = {} for type {} i = {}", row.getText(), name(), i);

                String key = row.getValue(keyCell).getAsKey();
                items.add(new Item(
                        key,
                        ItemScraper.ITEM_NAMES.getOrDefault(key, "minecraft:player_head"),
                        row.getValue(levelCell).getAsLevel(),
                        row.getValue(coinCell).getAsCoins()
                ));
            }

            return runAfterAll.apply(items);
        }
    }
}
