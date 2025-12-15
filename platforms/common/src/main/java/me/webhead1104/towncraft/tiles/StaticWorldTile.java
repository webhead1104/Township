package me.webhead1104.towncraft.tiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import net.kyori.adventure.text.Component;

@Getter
public class StaticWorldTile extends Tile {
    private final TowncraftMaterial material;

    public StaticWorldTile(TowncraftMaterial material) {
        this.material = material;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(material);
        itemStack.setName(Component.empty());
        itemStack.hideTooltip(true);
        return itemStack;
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        GRASS(new StaticWorldTile(TowncraftMaterial.GRASS_BLOCK)),
        STONE(new StaticWorldTile(TowncraftMaterial.STONE)),
        SAND(new StaticWorldTile(TowncraftMaterial.SAND)),
        WATER(new StaticWorldTile(TowncraftMaterial.COBBLESTONE)),
        ROAD(new StaticWorldTile(TowncraftMaterial.BLACK_CONCRETE)),
        PLANKS(new StaticWorldTile(TowncraftMaterial.OAK_PLANKS));
        private final StaticWorldTile tile;
    }
}
