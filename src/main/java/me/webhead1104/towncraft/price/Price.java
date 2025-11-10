package me.webhead1104.towncraft.price;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Price {
    void take(Player player);

    boolean has(Player player);

    Component getComponent(Player player);
}
