package me.webhead1104.township.features.factories;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.RecipeDataLoader;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.List;

@Getter
public enum FactoryType {
    BAKERY("Bakery", List.of(Township.key("bread"), Township.key("cookie"), Township.key("bagel"))),
    FEED_MILL_1("Feed Mill", List.of(Township.key("cow_feed"), Township.key("chicken_feed"))),
    FEED_MILL_2("Feed Mill", List.of(Township.key("cow_feed"), Township.key("chicken_feed"))),
    FEED_MILL_3("Feed Mill", List.of(Township.key("cow_feed"), Township.key("chicken_feed"))),
    DAIRY_FACTORY("Dairy Factory", List.of(Township.key("cream"), Township.key("cheese"))),
    SUGAR_FACTORY("Sugar Factory", List.of(Township.key("sugar")));
    private final String name;
    private final Component menuTitle;
    private final List<Key> recipes;

    FactoryType(String name, List<Key> recipes) {
        this.name = name;
        this.menuTitle = Msg.format("<gold>%s", name);
        this.recipes = recipes;
    }

    public List<RecipeDataLoader.Recipe> getRecipes() {
        return recipes.stream().map(RecipeDataLoader::get).toList();
    }
}
