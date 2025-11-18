package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.animals.AnimalMenu;
import me.webhead1104.towncraft.features.animals.AnimalType;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class AnimalTile extends BuildingTile {
    private final Key animalType;

    @Keep
    public AnimalTile(Key animalType) {
        super(Towncraft.getDataLoader(AnimalType.class).get(animalType).getBuildingKey());
        this.animalType = animalType;
    }

    @Override
    public ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Towncraft.getDataLoader(AnimalType.class).get(animalType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        context.openForPlayer(AnimalMenu.class, animalType);
        return true;
    }
}
