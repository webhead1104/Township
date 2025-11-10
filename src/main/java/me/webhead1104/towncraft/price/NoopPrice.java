package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NoopPrice implements Price {
    public static final NoopPrice INSTANCE = new NoopPrice();

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
