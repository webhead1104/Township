package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;

public class NoopPrice implements Price {
    public static final NoopPrice INSTANCE = new NoopPrice();

    @Override
    public void take(User user) {
    }

    @Override
    public boolean has(User user) {
        return true;
    }

    @Override
    public Component getComponent(User user) {
        return Msg.format("Free");
    }
}
