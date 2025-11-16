package me.webhead1104.towncraft.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.expansions.ExpansionMenu;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpansionTile extends Tile implements TimeFinishable {
    @Nullable
    private Instant instant;

    @Override
    public ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (instant == null) {
            ItemStack itemStack = ItemStack.of(Material.PODZOL);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Expansion"));
            return itemStack;
        }

        ItemStack itemStack = ItemStack.of(Material.DIRT);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Expansion"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time<white>: %s", Utils.format(Instant.now(), instant)))));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        if (instant != null) return false;
        int row = context.getClickedSlot() / 9;
        int col = context.getClickedSlot() % 9;

        int topLeftRow = row - (row % 3);
        int topLeftCol = col - (col % 3);

        context.openForPlayer(ExpansionMenu.class, topLeftRow * 9 + topLeftCol);
        return true;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        worldSection.setSlot(slot, StaticWorldTile.Type.GRASS.getTile());
    }
}
