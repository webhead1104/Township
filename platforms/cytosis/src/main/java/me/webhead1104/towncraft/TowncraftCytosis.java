package me.webhead1104.towncraft;

import me.webhead1104.towncraft.commands.TowncraftCommand;
import revxrsal.commands.Lamp;
import revxrsal.commands.minestom.MinestomLamp;
import revxrsal.commands.minestom.actor.MinestomCommandActor;

public class TowncraftCytosis {

    public static void initialize() {
        TowncraftPlatformManager.init();

        Lamp<MinestomCommandActor> lamp = MinestomLamp.builder()
                .parameterTypes(TowncraftPlatformManager::registerParameterTypes).build();
        lamp.register(new TowncraftCommand());
        TowncraftPlatformManager.initCommands(lamp);
    }

    public static void shutdown() {
        System.out.println("STOP");
    }
}
