package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TowncraftItemStackTestImpl implements TowncraftItemStack {
    private TowncraftMaterial material;
    private Component name;
    private List<Component> lore = new ArrayList<>();
    private String playerHeadTexture;

    public TowncraftItemStackTestImpl(TowncraftMaterial material) {
        this.material = material;
        this.name = Msg.format(Utils.thing2(material.getKey().value()));
    }

    @Override
    public void setName(Component component) {
        this.name = component;
    }

    @Override
    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    @Override
    public void overrideNameColor() {
    }

    @Override
    public boolean isSimilar(@NotNull TowncraftItemStack other) {
        return false;
    }

    @Override
    public TowncraftMaterial getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(TowncraftMaterial material) {
        this.material = material;
    }

    @Override
    public void setPlayerHeadTexture(String texture) {
    }

    @Override
    public void hideTooltip(boolean hideTooltip) {
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Object toPlatform() {
        return null;
    }
}
