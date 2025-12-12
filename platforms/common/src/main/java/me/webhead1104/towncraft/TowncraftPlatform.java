package me.webhead1104.towncraft;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public interface TowncraftPlatform extends Scheduler {
    @Nullable
    TowncraftPlayer getPlayer(UUID uuid);

    Logger getLogger();

    File getDataFolder();
}
