package me.webhead1104.towncraft.menus.util;

import org.jetbrains.annotations.NotNull;

public final class IsTypeOf {

    private IsTypeOf() {
    }

    public static boolean isTypeOf(@NotNull Class<?> superCls, @NotNull Class<?> cls) {
        return superCls.isAssignableFrom(cls);
    }
}
