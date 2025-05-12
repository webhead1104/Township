package me.webhead1104.township.data.impls;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.interfaces.Price;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Getter
@Setter
public class CoinPrice implements Price {
    private int amount;

    public CoinPrice(int amount) {
        this.amount = amount;
    }

    @Override
    public void take(Player player) {
        Township.getUserManager().getUser(player.getUniqueId()).setCoins(Township.getUserManager().getUser(player.getUniqueId()).getCoins() - amount);
    }

    @Override
    public boolean has(Player player) {
        return Township.getUserManager().getUser(player.getUniqueId()).getCoins() >= amount;
    }

    @Override
    public Component neededComponent(Player player) {
        if (amount == 0) {
            return Msg.format("<white>Free");
        }
        return Msg.format(String.format("<gold>Coins needed<white>: %s<aqua>/<white>%s", amount, Township.getUserManager().getUser(player.getUniqueId()).getCoins()));
    }

    @Override
    public Component notEnoughComponent(Player player) {
        return Msg.format("<red>You need <gold>%s<gold> more coins to purchase this!", Township.getUserManager().getUser(player.getUniqueId()).getCoins() - amount);
    }
}
