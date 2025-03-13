package me.webhead1104.township.data.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Getter
@AllArgsConstructor
public enum FactoryType {
    BAKERY(Msg.format("<gold>Bakery"), "bakery", List.of(RecipeType.BREAD, RecipeType.COOKIE, RecipeType.BAGEL), 2, 55, 125, 10, WorldTileType.BAKERY),
    FEED_MILL_1(Msg.format("<gold>Feed Mill"), "feed_mill_1", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 3, 60, 150, 11, WorldTileType.FEED_MILL_1),
    FEED_MILL_2(Msg.format("<gold>Feed Mill"), "feed_mill_2", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 19, 655, 2800, 120, WorldTileType.FEED_MILL_2),
    FEED_MILL_3(Msg.format("<gold>Feed Mill"), "feed_mill_3", List.of(RecipeType.COW_FEED, RecipeType.CHICKEN_FEED), 44, 4150, 40000, 1655, WorldTileType.FEED_MILL_3),
    DAIRY_FACTORY(Msg.format("<gold>Dairy Factory"), "dairy_factory", List.of(RecipeType.CREAM, RecipeType.CHEESE), 4, 65, 175, 12, WorldTileType.DAIRY_FACTORY),
    SUGAR_FACTORY(Msg.format("<gold>Sugar Factory"), "sugar_factory", List.of(RecipeType.SUGAR), 7, 75, 250, 15, WorldTileType.SUGAR_FACTORY);
    public static final @NotNull Codec<FactoryType> CODEC = Codec.STRING.xmap(FactoryType::valueOf, FactoryType::toString);
    private final Component menuTitle;
    private final String ID;
    private final List<RecipeType> recipes;
    private final int levelNeeded;
    private final int populationNeeded;
    private final int coinsNeeded;
    private final int xpGiven;
    private final WorldTileType tileType;
}
