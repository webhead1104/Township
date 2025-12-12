package me.webhead1104.towncraft.impl.items;

import lombok.AllArgsConstructor;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.ItemRarity;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.Sets;

import java.util.List;

@AllArgsConstructor
public class TowncraftItemStackImpl implements TowncraftItemStack {
    private ItemStack itemStack;

    @Override
    public void setName(Component component) {
        itemStack = itemStack.with(DataComponents.ITEM_NAME, component);
    }

    @Override
    public void setLore(List<Component> lore) {
        itemStack = itemStack.with(DataComponents.LORE, lore);
    }

    @Override
    public void overrideNameColor() {
        itemStack = itemStack.with(DataComponents.RARITY, ItemRarity.COMMON);
    }

    @Override
    public boolean isSimilar(@NotNull TowncraftItemStack other) {
        return itemStack.isSimilar((ItemStack) other.toPlatform());
    }

    @Override
    public TowncraftMaterial getMaterial() {
        return new TowncraftMaterialImpl(itemStack.material());
    }

    @Override
    public void setMaterial(TowncraftMaterial material) {
        itemStack = itemStack.withMaterial(((TowncraftMaterialImpl) material).material());
    }

    @Override
    public void setPlayerHeadTexture(String texture) {
        ResolvableProfile profile = new ResolvableProfile(new ResolvableProfile.Partial(null, null, List.of(new GameProfile.Property("textures", texture))));
        itemStack = itemStack.with(DataComponents.PROFILE, profile);
    }

    @Override
    public void hideTooltip(boolean hideTooltip) {
        itemStack = itemStack.with(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(hideTooltip, Sets.newHashSet()));
    }

    @Override
    public boolean isEmpty() {
        return itemStack.equals(ItemStack.AIR) || itemStack.material() == Material.AIR || itemStack.amount() <= 0;
    }

    @Override
    public Object toPlatform() {
        return itemStack;
    }
}
