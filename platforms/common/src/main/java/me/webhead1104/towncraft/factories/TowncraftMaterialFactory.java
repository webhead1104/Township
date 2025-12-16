package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TowncraftMaterialFactory {
    TowncraftMaterialFactory INSTANCE = Services.service(TowncraftMaterialFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftMaterialFactory found!"));

    @Nullable
    static TowncraftMaterial get(@NotNull Key key) {
        return INSTANCE.get0(key);
    }

    @Nullable
    TowncraftMaterial get0(@NotNull Key key);
}
