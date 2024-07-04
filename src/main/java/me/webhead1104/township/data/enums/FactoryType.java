package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import java.util.List;
import static net.cytonic.cytosis.utils.MiniMessageTemplate.MM;

@Getter
public enum FactoryType {

    NONE(MM."none", MenuItems.air,"NONE",List.of(MenuItems.air)),
    BAKERY(MM."<gold>Bakery", MenuItems.bakeryMenuItem,"BAKERY",List.of(MenuItems.breadRecipe,MenuItems.cookieRecipe,MenuItems.bagelRecipe)),
    FEED_MILL(MM."<gold>Feed Mill",MenuItems.feedmillMenuItem,"FEED_MILL",List.of(MenuItems.cowfeedrecipe,MenuItems.chickenfeedrecipe)),
    DAIRY_FACTORY(MM."<gold>Dairy Factory",MenuItems.dairyMenuItem,"DAIRY_FACTORY",List.of(MenuItems.creamRecipe,MenuItems.cheeseRecipe,MenuItems.butterRecipe,MenuItems.yogurtRecipe)),
    SUGAR_FACTORY(MM."<gold>Sugar Factory",MenuItems.sugarMenuItem,"SUGAR_FACTORY",List.of(MenuItems.sugarRecipe,MenuItems.syrupRecipe,MenuItems.caramelRecipe));

    private final Component factoryName;
    private final ItemStack menuItem;
    private final String ID;
    private final List<ItemStack> recipes;
    FactoryType(Component factoryName, ItemStack menuItem, String ID, List<ItemStack> recipes) {
        this.factoryName = factoryName;
        this.menuItem = menuItem;
        this.ID = ID;
        this.recipes = recipes;
    }

}
