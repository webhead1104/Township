package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.TowncraftPlayer;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TowncraftPlayerFactory {
    TowncraftPlayerFactory INSTANCE = Services.service(TowncraftPlayerFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftPlayerFactory found!"));

    @NotNull
    TowncraftPlayer of(UUID uuid);
}
