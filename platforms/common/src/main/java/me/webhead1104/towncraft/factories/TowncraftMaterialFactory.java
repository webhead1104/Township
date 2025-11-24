package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

public interface TowncraftMaterialFactory {
    TowncraftMaterialFactory INSTANCE = Services.service(TowncraftMaterialFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftMaterialFactory found!"));

    @NotNull
    static TowncraftMaterial of(@NotNull @KeyPattern.Value String key) {
        return INSTANCE.of0(Key.key(key));
    }

    @NotNull
    static TowncraftMaterial of(@NotNull Key key) {
        return INSTANCE.of0(key);
    }

    @NotNull
    TowncraftMaterial of0(@NotNull Key key);
}
