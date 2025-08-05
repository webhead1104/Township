package me.webhead1104.township.price;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Getter
public class CoinPrice implements Price {
    private final int amount;

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
    public Component component(Player player) {
        if (amount == 0) {
            return Msg.format("Free");
        }
        if (has(player)) {
            return Msg.format("<gold>Coins<white>: <green>%d/%d", amount, Township.getUserManager().getUser(player.getUniqueId()).getCoins());
        }
        return Msg.format("<gold>Coins<white>: <red>%d/%d", amount, Township.getUserManager().getUser(player.getUniqueId()).getCoins());
    }
}
