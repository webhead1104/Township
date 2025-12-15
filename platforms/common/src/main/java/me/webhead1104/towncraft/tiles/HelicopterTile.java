package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.helicopter.HelicopterView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;

public class HelicopterTile extends BuildingTile {

    @Keep
    public HelicopterTile() {
        super(Towncraft.key("helicopter"));
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format("Helicopter"));
        String textures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UxYjQ2ODI0MmEzYzNhYTQ1NDRmNTM4OWExMmJiNGY4Njc1ZjBiMTRlZjdmM2EyN2ZlMTk0Mjg0ODAyMDQ2YiJ9fX0=";
        itemStack.setPlayerHeadTexture(textures);
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        context.openForPlayer(HelicopterView.class);
        return true;
    }
}
