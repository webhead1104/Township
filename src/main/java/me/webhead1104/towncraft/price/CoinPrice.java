package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public record CoinPrice(int amount) implements Price {

    @Override
    public void take(Player player) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).setCoins(Towncraft.getUserManager().getUser(player.getUniqueId()).getCoins() - amount);
    }

    @Override
    public boolean has(Player player) {
        return Towncraft.getUserManager().getUser(player.getUniqueId()).getCoins() >= amount;
    }

    @Override
    public Component getComponent(Player player) {
        if (amount == 0) {
            return Msg.format("Free");
        }
        return Utils.addResourceLine("<gold>Coins", Towncraft.getUserManager().getUser(player.getUniqueId()).getCoins(), amount);
    }
}
