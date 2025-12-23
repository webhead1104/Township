package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import me.webhead1104.towncraft.impl.items.TowncraftMaterialTestImpl;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TowncraftMaterialFactoryTestImpl implements TowncraftMaterialFactory {
    @Override
    public @Nullable TowncraftMaterial get0(@NotNull Key key) {
        return new TowncraftMaterialTestImpl(key);
    }
}
