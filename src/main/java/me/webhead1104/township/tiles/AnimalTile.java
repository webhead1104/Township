package me.webhead1104.township.tiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.objects.Animals;
import me.webhead1104.township.menus.AnimalMenu;
import me.webhead1104.township.tiles.TileUtils;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class AnimalTile extends Tile {
    private final AnimalType animalType;

    @Override
    public ItemStack render(SlotRenderContext context) {
        Animals.AnimalBuilding animalBuilding = Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getAnimals().getAnimalBuilding(animalType);
        return TileUtils.createPurchableItemStack(animalType.getName(), !animalBuilding.isUnlocked());
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
