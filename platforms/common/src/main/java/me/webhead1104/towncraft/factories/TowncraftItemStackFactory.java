package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

public interface TowncraftItemStackFactory {
    TowncraftItemStackFactory INSTANCE = Services.service(TowncraftItemStackFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftItemStackFactory found!"));

    @NotNull
    static TowncraftItemStack get(TowncraftMaterial material) {
        return INSTANCE.get0(material);
    }

    @NotNull
    TowncraftItemStack get0(TowncraftMaterial material);
}
