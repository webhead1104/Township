package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

import java.util.List;

@Getter
public enum FactoryType {
    BAKERY("Bakery", List.of(RecipeType.BREAD, RecipeType.COOKIE, RecipeType.BAGEL), 2, 55, 125, 10),
    FEED_MILL_1("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 3, 60, 150, 11),
    FEED_MILL_2("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 19, 655, 2800, 120),
    FEED_MILL_3("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 44, 4150, 40000, 1655),
    DAIRY_FACTORY("Dairy Factory", List.of(RecipeType.CREAM, RecipeType.CHEESE), 4, 65, 175, 12),
    SUGAR_FACTORY("Sugar Factory", List.of(RecipeType.SUGAR), 7, 75, 250, 15);
    private final String name;
    private final Component menuTitle;
    private final List<RecipeType> recipes;
    private final int levelNeeded;
    private final int populationNeeded;
    private final int coinsNeeded;
    private final int xpGiven;

    FactoryType(String name, List<RecipeType> recipes, int levelNeeded, int populationNeeded, int coinsNeeded, int xpGiven) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.recipes = recipes;
        this.levelNeeded = levelNeeded;
        this.populationNeeded = populationNeeded;
        this.coinsNeeded = coinsNeeded;
        this.xpGiven = xpGiven;
    }
}
