package me.webhead1104.towncraft.commands.arguments;

import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import org.jetbrains.annotations.NotNull;

public final class ItemTypeArgument extends TowncraftDataLoaderArgument<ItemType.Item> {
    @Override
    public @NotNull Class<? extends DataLoader.KeyBasedDataLoader<?>> getDataLoaderClass() {
        return ItemType.class;
    }
}
