package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.features.animals.AnimalType;
import org.jetbrains.annotations.NotNull;

public final class AnimalTypeArgument extends TowncraftDataLoaderArgument<AnimalType.Animal> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return AnimalType.class;
    }
}
