package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public record TowncraftMaterialImpl(Material material) implements TowncraftMaterial {
    public TowncraftMaterialImpl(Key key) {
        this(Material.fromKey(key));
    }

    @Override
    public @NotNull Key getKey() {
        return material.key();
    }

    @Override
    public Object getPlatform() {
        return material;
    }
}
