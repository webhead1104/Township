package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;

public record TowncraftMaterialTestImpl(Key key) implements TowncraftMaterial {

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public Object getPlatform() {
        throw new UnsupportedOperationException();
    }
}
