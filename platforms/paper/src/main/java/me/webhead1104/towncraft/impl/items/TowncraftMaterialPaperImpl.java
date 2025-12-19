package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public record TowncraftMaterialPaperImpl(Material material) implements TowncraftMaterial {
    public TowncraftMaterialPaperImpl(Key key) {
        this(Registry.MATERIAL.get(key));
    }

    @Override
    public @NotNull Key getKey() {
        return material.getKey();
    }

    @Override
    public Object getPlatform() {
        return material;
    }
}
