package me.webhead1104.township.price;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Price {
    void take(Player player);

    boolean has(Player player);

    Component component(Player player);
}
