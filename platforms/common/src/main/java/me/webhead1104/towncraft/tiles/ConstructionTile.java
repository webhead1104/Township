package me.webhead1104.towncraft.tiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ConstructionTile extends Tile implements TimeFinishable {
    private String name;
    private Instant instant;
    private Tile tile;

    public ConstructionTile(String name, Duration duration, Tile tile) {
        this.name = name;
        this.instant = Instant.now().plus(duration);
        this.tile = tile;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (instant == null) {
            handleUpdate(context, worldSection, slot);
            return null;
        }
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(name));
        itemStack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), instant)));
        return itemStack;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        worldSection.setSlot(slot, tile);
    }
}
