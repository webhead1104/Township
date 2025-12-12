package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

public interface TowncraftItemStackFactory {
    TowncraftItemStackFactory INSTANCE = Services.service(TowncraftItemStackFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftItemStackFactory found!"));

    @NotNull
    static TowncraftItemStack of(@NotNull @KeyPattern.Value String key) {
        return of(TowncraftMaterialFactory.of(Key.key(key)));
    }

    @NotNull
    static TowncraftItemStack of(@NotNull Key key) {
        return of(TowncraftMaterialFactory.of(key));
    }

    @NotNull
    static TowncraftItemStack of(TowncraftMaterial material) {
        return INSTANCE.of0(material);
    }

    @NotNull
    TowncraftItemStack of0(TowncraftMaterial material);
}
