package me.webhead1104.towncraft.items;

import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public interface TowncraftItemStack {
    @NotNull
    static TowncraftItemStack of(TowncraftMaterial material) {
        TowncraftItemStack itemStack = TowncraftItemStackFactory.of(material);
        if (material == TowncraftMaterial.PLAYER_HEAD) {
            itemStack.overrideNameColor();
        }
        return itemStack;
    }

    static TowncraftItemStack empty() {
        return TowncraftItemStack.of(TowncraftMaterials.AIR);
    }

    void setName(Component component);

    void setLore(List<Component> lore);

    default void setLore(Component... lore) {
        setLore(Arrays.asList(lore));
    }

    void overrideNameColor();

    boolean isSimilar(@NotNull TowncraftItemStack other);

    TowncraftMaterial getMaterial();

    void setMaterial(TowncraftMaterial material);

    void setPlayerHeadTexture(String texture);

    void hideTooltip(boolean hideTooltip);

    boolean isEmpty();

    Object toPlatform();
}
