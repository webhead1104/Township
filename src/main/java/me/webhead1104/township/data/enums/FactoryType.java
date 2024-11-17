package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.List;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@Getter
@AllArgsConstructor
public enum FactoryType {
    BAKERY(MM."<gold>Bakery", "bakery", List.of(RecipeType.BREAD, RecipeType.COOKIE, RecipeType.BAGEL), 2, WorldTileType.BAKERY),
    FEED_MILL(MM."<gold>Feed Mill", "feed_mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 3, WorldTileType.FEED_MILL),
    DAIRY_FACTORY(MM."<gold>Dairy Factory", "dairy_factory", List.of(RecipeType.CREAM, RecipeType.CHEESE), 4, WorldTileType.DAIRY_FACTORY),
    SUGAR_FACTORY(MM."<gold>Sugar Factory", "sugar_factory", List.of(RecipeType.SUGAR), 7, WorldTileType.SUGAR_FACTORY);

    private final Component menuTitle;
    private final String ID;
    private final List<RecipeType> recipes;
    private final int levelNeeded;
    private final WorldTileType tileType;
}
