package me.webhead1104.towncraft.items;

import net.kyori.adventure.key.Key;

public non-sealed interface TowncraftMaterial extends TowncraftMaterials {
    Key getKey();

    Object getPlatform();
}
