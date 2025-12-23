package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackTestImpl;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import org.jetbrains.annotations.NotNull;

public class TowncraftItemStackFactoryTestImpl implements TowncraftItemStackFactory {

    @Override
    public @NotNull TowncraftItemStack get0(TowncraftMaterial material) {
        return new TowncraftItemStackTestImpl(material);
    }
}
