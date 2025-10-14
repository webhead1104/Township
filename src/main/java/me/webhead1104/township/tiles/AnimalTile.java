package me.webhead1104.township.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.features.animals.AnimalMenu;
import me.webhead1104.township.features.animals.AnimalType;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class AnimalTile extends BuildingTile {
    private final Key animalType;

    @Keep
    public AnimalTile(Key animalType) {
        super(Township.getDataLoader(AnimalType.class).get(animalType).getBuildingKey(), 0);
        this.animalType = animalType;
    }

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Township.getDataLoader(AnimalType.class).get(animalType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        context.openForPlayer(AnimalMenu.class, animalType);
        return true;
    }
}
