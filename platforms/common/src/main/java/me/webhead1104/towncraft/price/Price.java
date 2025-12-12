package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.data.objects.User;
import net.kyori.adventure.text.Component;

public interface Price {
    void take(User player);

    boolean has(User player);

    Component getComponent(User player);
}
