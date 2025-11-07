package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.data.TileSize;
import me.webhead1104.township.features.world.plots.PlotType;
import me.webhead1104.township.tiles.*;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigSerializable
@NoArgsConstructor
public class WorldSection {
    private final Map<Integer, Tile> slotMap = new HashMap<>();
    private int section;

    public WorldSection(int section) {
        for (int i = 0; i < 54; i++) {
            if (slotMap.containsKey(i)) continue;
            slotMap.put(i, StaticWorldTile.Type.GRASS.getTile());
            if (section != 27 && section != 28 && section != 35 && section != 36) {
                for (Integer a : TileSize.SIZE_3X3.toList(i)) {
                    ExpansionTile tile = new ExpansionTile(null);
                    slotMap.put(a, tile);
                }
            }
            if (section == 27) {
                PlotTile plotTile = new PlotTile(new Plot(section, 34, PlotType.NONE));
                slotMap.put(34, plotTile);
                for (int a : TileSize.SIZE_3X3.toList(0)) {
                    slotMap.put(a, new BarnTile());
                }
            }
            if (section == 28) {
                for (int a : TileSize.SIZE_1X3.toList(0)) {
                    slotMap.put(a, new TrainTile());
                }
            }
        }
        this.section = section;
    }

    public final Map<Integer, Tile> getSlotMap() {
        Map<Integer, Tile> map = new HashMap<>();
        for (int i = 0; i < 54; i++) {
            map.put(i, getSlot(i));
        }
        return map;
    }

    public void setSlot(int slot, Tile tile) {
        slotMap.put(slot, tile);
    }

    public Tile getSlot(int slot) {
        if (slotMap.containsKey(slot)) {
            return slotMap.get(slot);
        }
        return new ExpansionTile();
    }
}
