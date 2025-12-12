package me.webhead1104.towncraft.dataLoaders;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Keyed implements net.kyori.adventure.key.Keyed {

    public abstract Key getKey();

    @Override
    public @NotNull Key key() {
        return getKey();
    }

    public boolean equals(Key key) {
        return Objects.equals(getKey(), key);
    }
}
