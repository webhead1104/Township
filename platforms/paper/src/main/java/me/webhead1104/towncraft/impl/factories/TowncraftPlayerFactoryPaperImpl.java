package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.factories.TowncraftPlayerFactory;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TowncraftPlayerFactoryPaperImpl implements TowncraftPlayerFactory {

    @Override
    public @NotNull TowncraftPlayer of(UUID uuid) {
        return new TowncraftPlayerPaperImpl(Bukkit.getPlayer(uuid));
    }
}
