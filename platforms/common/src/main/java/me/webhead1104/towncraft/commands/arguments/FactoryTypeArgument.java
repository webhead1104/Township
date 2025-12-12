package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.features.factories.FactoryType;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

public final class FactoryTypeArgument<A extends CommandActor> extends TowncraftDataLoaderArgument<A, FactoryType.Factory> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return FactoryType.class;
    }

    @Override
    public @NotNull Class<FactoryType.Factory> resultClass() {
        return FactoryType.Factory.class;
    }
}
