package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

public final class ItemTypeArgument<A extends CommandActor> extends TowncraftDataLoaderArgument<A, ItemType.Item> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return ItemType.class;
    }

    @Override
    public @NotNull Class<ItemType.Item> resultClass() {
        return ItemType.Item.class;
    }
}
