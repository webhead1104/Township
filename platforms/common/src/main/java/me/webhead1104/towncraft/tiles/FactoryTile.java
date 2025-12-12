package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.factories.FactoryMenu;
import me.webhead1104.towncraft.features.factories.FactoryType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;

@Getter
public class FactoryTile extends BuildingTile {
    private final Key factoryType;

    @Keep
    public FactoryTile(Key factoryType) {
        super(Towncraft.getDataLoader(FactoryType.class).get(factoryType).getBuildingKey());
        this.factoryType = factoryType;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(Towncraft.getDataLoader(FactoryType.class).get(factoryType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        context.openForPlayer(FactoryMenu.class, factoryType);
        return true;
    }
}
