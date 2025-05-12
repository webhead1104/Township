package me.webhead1104.township.data.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Price {
    void take(Player player);

    boolean has(Player player);

    Component neededComponent(Player player);

    Component notEnoughComponent(Player player);
}
