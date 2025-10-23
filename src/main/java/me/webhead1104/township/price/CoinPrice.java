package me.webhead1104.township.price;

import me.webhead1104.township.Township;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public record CoinPrice(int amount) implements Price {

    @Override
    public void take(Player player) {
        Township.getUserManager().getUser(player.getUniqueId()).setCoins(Township.getUserManager().getUser(player.getUniqueId()).getCoins() - amount);
    }

    @Override
    public boolean has(Player player) {
        return Township.getUserManager().getUser(player.getUniqueId()).getCoins() >= amount;
    }

    @Override
    public Component getComponent(Player player) {
        if (amount == 0) {
            return Msg.format("Free");
        }
        return Utils.addResourceLine("<gold>Coins", Township.getUserManager().getUser(player.getUniqueId()).getCoins(), amount);
    }
}
