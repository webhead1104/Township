package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.features.factories.FactoryType;
import org.jetbrains.annotations.NotNull;

public final class FactoryTypeArgument extends TowncraftDataLoaderArgument<FactoryType.Factory> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return FactoryType.class;
    }
}
