package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.menus.AnimalMenu;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class AnimalTile extends Tile {
    private final AnimalType animalType;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(animalType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        if (Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getAnimals().getAnimalBuilding(animalType).isUnlocked()) {
            context.openForPlayer(AnimalMenu.class, animalType);
            return true;
        }
        return false;
    }
}
