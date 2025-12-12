package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.text.Component;

public record CoinPrice(int amount) implements Price {

    @Override
    public void take(User user) {
        user.setCoins(user.getCoins() - amount);
    }

    @Override
    public boolean has(User user) {
        return user.getCoins() >= amount;
    }

    @Override
    public Component getComponent(User user) {
        if (amount == 0) {
            return Msg.format("Free");
        }
        return Utils.addResourceLine("<gold>Coins", user.getCoins(), amount);
    }
}
