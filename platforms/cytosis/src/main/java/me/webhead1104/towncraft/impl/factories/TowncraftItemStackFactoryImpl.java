package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackImpl;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class TowncraftItemStackFactoryImpl implements TowncraftItemStackFactory {
    @Override
    public @NotNull TowncraftItemStack get0(TowncraftMaterial material) {
        return new TowncraftItemStackImpl(ItemStack.of((Material) material.getPlatform()));
    }
}
