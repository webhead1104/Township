package me.webhead1104.township.features.factories;

import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

import java.util.List;

@Getter
public enum FactoryType {
    BAKERY("Bakery", List.of(RecipeType.BREAD, RecipeType.COOKIE, RecipeType.BAGEL)),
    FEED_MILL_1("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED)),
    FEED_MILL_2("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED)),
    FEED_MILL_3("Feed Mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED)),
    DAIRY_FACTORY("Dairy Factory", List.of(RecipeType.CREAM, RecipeType.CHEESE)),
    SUGAR_FACTORY("Sugar Factory", List.of(RecipeType.SUGAR));
    private final String name;
    private final Component menuTitle;
    private final List<RecipeType> recipes;

    FactoryType(String name, List<RecipeType> recipes) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.recipes = recipes;
    }
}
