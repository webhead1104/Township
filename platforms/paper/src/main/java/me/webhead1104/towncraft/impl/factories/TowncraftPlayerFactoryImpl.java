package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.factories.TowncraftPlayerFactory;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TowncraftPlayerFactoryImpl implements TowncraftPlayerFactory {

    @Override
    public @NotNull TowncraftPlayer of(UUID uuid) {
        return new TowncraftPlayerImpl(Bukkit.getPlayer(uuid));
    }
}
