package me.webhead1104.towncraft;

import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public interface TowncraftPlatform extends Scheduler {
    TowncraftPlayer getPlayer(UUID uuid);

    Logger getLogger();

    File getDataFolder();
}
