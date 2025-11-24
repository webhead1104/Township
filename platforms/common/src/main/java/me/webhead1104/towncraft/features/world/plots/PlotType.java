package me.webhead1104.towncraft.features.world.plots;

import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.price.CoinPrice;
import me.webhead1104.towncraft.price.NoopPrice;
import me.webhead1104.towncraft.price.Price;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;

import java.time.Duration;

@Getter
public enum PlotType {
    NONE(Towncraft.NONE_KEY, NoopPrice.INSTANCE, -1, -1, Duration.ZERO),
    WHEAT(Towncraft.key("wheat"), new CoinPrice(0), 1, 1, Duration.ofMinutes(2)),
    CORN(Towncraft.key("corn"), new CoinPrice(1), 3, 1, Duration.ofMinutes(5)),
    CARROT(Towncraft.key("carrot"), new CoinPrice(2), 4, 2, Duration.ofMinutes(10)),
    SUGARCANE(Towncraft.key("sugarcane"), new CoinPrice(3), 7, 3, Duration.ofMinutes(20)),
    COTTON(Towncraft.key("cotton"), new CoinPrice(4), 9, 4, Duration.ofMinutes(30));
    private final ItemType.Item item;
    private final TowncraftItemStack menuItem;
    private final Price price;
    private final int levelNeeded;
    private final int xpGiven;
    private final Duration time;

    PlotType(Key key, Price price, int levelNeeded, int xpGiven, Duration time) {
        this.item = Towncraft.getDataLoader(ItemType.class).get(key);
        if (key == Towncraft.NONE_KEY) {
            TowncraftItemStack itemStack = TowncraftItemStackFactory.of(TowncraftMaterial.FARMLAND);
            itemStack.setName(Msg.format("Empty Plot"));
            itemStack.setLore(Msg.format("<green>You should plant something!"));
            menuItem = itemStack;
        } else {
            this.menuItem = item.getItemStack();
        }
        this.price = price;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
        this.time = time;
    }
}
