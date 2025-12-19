package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import me.webhead1104.towncraft.impl.items.TowncraftMaterialPaperImpl;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class TowncraftMaterialFactoryPaperImpl implements TowncraftMaterialFactory {
    @Override
    public @NotNull TowncraftMaterial get0(@NotNull Key key) {
        return new TowncraftMaterialPaperImpl(key);
    }
}
