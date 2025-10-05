package me.webhead1104.township.price;

import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NoopPrice implements Price {
    @Override
    public void take(Player player) {
    }

    @Override
    public boolean has(Player player) {
        return true;
    }

    @Override
    public Component getComponent(Player player) {
        return Msg.format("Free");
    }
}
