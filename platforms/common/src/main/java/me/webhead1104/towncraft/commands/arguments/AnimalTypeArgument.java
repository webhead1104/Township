package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.features.animals.AnimalType;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

public final class AnimalTypeArgument<A extends CommandActor> extends TowncraftDataLoaderArgument<A, AnimalType.Animal> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return AnimalType.class;
    }

    @Override
    public @NotNull Class<AnimalType.Animal> resultClass() {
        return AnimalType.Animal.class;
    }
}
