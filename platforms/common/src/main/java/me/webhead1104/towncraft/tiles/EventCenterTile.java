package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;

@Keep
public class EventCenterTile extends Tile {

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format("Event Center"));
        itemStack.setLore(Msg.format("<red>The Event Center is currently closed."));
        return itemStack;
    }
}
