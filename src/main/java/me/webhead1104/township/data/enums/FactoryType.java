package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

import java.util.List;


@Getter
@AllArgsConstructor
public enum FactoryType {
    BAKERY(Msg.format("<gold>Bakery"), "bakery", List.of(RecipeType.BREAD, RecipeType.COOKIE, RecipeType.BAGEL), 2, WorldTileType.BAKERY),
    FEED_MILL(Msg.format("<gold>Feed Mill"), "feed_mill", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 3, WorldTileType.FEED_MILL),
    DAIRY_FACTORY(Msg.format("<gold>Dairy Factory"), "dairy_factory", List.of(RecipeType.CREAM, RecipeType.CHEESE), 4, WorldTileType.DAIRY_FACTORY),
    SUGAR_FACTORY(Msg.format("<gold>Sugar Factory"), "sugar_factory", List.of(RecipeType.SUGAR), 7, WorldTileType.SUGAR_FACTORY);

    private final Component menuTitle;
    private final String ID;
    private final List<RecipeType> recipes;
    private final int levelNeeded;
    private final WorldTileType tileType;
}
