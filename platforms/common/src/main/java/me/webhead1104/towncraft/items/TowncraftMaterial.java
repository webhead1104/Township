package me.webhead1104.towncraft.items;

import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface TowncraftMaterial extends TowncraftMaterials {

    @NotNull
    static TowncraftMaterial of(@NotNull Key key) {
        return TowncraftMaterialFactory.of(key);
    }

    @NotNull
    Key getKey();

    Object getPlatform();
}
