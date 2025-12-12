package me.webhead1104.towncraft;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.commands.TowncraftCommand;
import revxrsal.commands.Lamp;
import revxrsal.commands.minestom.MinestomLamp;
import revxrsal.commands.minestom.actor.MinestomCommandActor;

public class TowncraftCytosis {

    @Keep
    public static void initialize() {
        TowncraftPlatformManager.init();

        Lamp<MinestomCommandActor> lamp = MinestomLamp.builder()
                .parameterTypes(TowncraftPlatformManager::registerParameterTypes).build();
        lamp.register(new TowncraftCommand());
        TowncraftPlatformManager.initCommands(lamp);
    }

    @Keep
    public static void shutdown() {
        TowncraftPlatformManager.shutdown();
    }
}
