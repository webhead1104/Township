package me.webhead1104.township.features.world.plots;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.price.CoinPrice;
import me.webhead1104.township.price.NoopPrice;
import me.webhead1104.township.price.Price;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;

@Getter
public enum PlotType {
    NONE(Township.noneKey, NoopPrice.INSTANCE, -1, -1, Duration.ZERO),
    WHEAT(Township.key("wheat"), new CoinPrice(0), 1, 1, Duration.ofMinutes(2)),
    CORN(Township.key("corn"), new CoinPrice(1), 3, 1, Duration.ofMinutes(5)),
    CARROT(Township.key("carrot"), new CoinPrice(2), 4, 2, Duration.ofMinutes(10)),
    SUGARCANE(Township.key("sugarcane"), new CoinPrice(3), 7, 3, Duration.ofMinutes(20)),
    COTTON(Township.key("cotton"), new CoinPrice(4), 9, 4, Duration.ofMinutes(30));
    private final ItemType.Item item;
    private final ItemStack menuItem;
    private final Price price;
    private final int levelNeeded;
    private final int xpGiven;
    private final Duration time;

    PlotType(Key key, Price price, int levelNeeded, int xpGiven, Duration time) {
        this.item = Township.getDataLoader(ItemType.class).get(key);
        if (key == Township.noneKey) {
            ItemStack itemStack = ItemStack.of(Material.FARMLAND);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Empty Plot"));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>You should plant something!"))));
            menuItem = itemStack;
        } else {
            this.menuItem = item.getItemStack();
        }
        this.price = price;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
        this.time = time;
    }

    public ItemStack getMenuItem() {
        return menuItem.clone();
    }
}
