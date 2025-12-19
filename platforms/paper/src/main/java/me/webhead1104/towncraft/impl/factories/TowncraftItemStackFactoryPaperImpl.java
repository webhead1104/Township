package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackPaperImpl;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TowncraftItemStackFactoryPaperImpl implements TowncraftItemStackFactory {
    @Override
    public @NotNull TowncraftItemStack get0(TowncraftMaterial material) {
        ItemStack itemStack = ItemStack.of((Material) material.getPlatform());
        return new TowncraftItemStackPaperImpl(itemStack);
    }
}
