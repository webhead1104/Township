package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.animals.AnimalMenu;
import me.webhead1104.towncraft.features.animals.AnimalType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;

@Getter
public class AnimalTile extends BuildingTile {
    private final Key animalType;

    @Keep
    public AnimalTile(Key animalType) {
        super(Towncraft.getDataLoader(AnimalType.class).get(animalType).getBuildingKey());
        this.animalType = animalType;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(Towncraft.getDataLoader(AnimalType.class).get(animalType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        context.openForPlayer(AnimalMenu.class, animalType);
        return true;
    }
}
