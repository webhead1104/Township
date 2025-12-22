package me.webhead1104.towncraft.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.data.TileSize;
import me.webhead1104.towncraft.tiles.*;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;

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
                    ExpansionTile tile = new ExpansionTile();
                    slotMap.put(a, tile);
                }
            }
            if (section == 27) {
                PlotTile plotTile = new PlotTile();
                slotMap.put(34, plotTile);
                for (int a : TileSize.SIZE_3X3.toList(0)) {
                    slotMap.put(a, new BarnTile());
                }
            }
            if (section == 28) {
                for (int a : TileSize.SIZE_1X3.toList(0)) {
                    slotMap.put(a, new TrainTile());
                }
                for (int integer : new TileSize(3, 5).toList(3)) {
                    slotMap.put(integer, new EventCenterTile());
                }
            }
        }
        this.section = section;
        postProcess();
    }

    @PostProcess
    private void postProcess() {
        for (int i = 0; i < 54; i++) {
            slotMap.putIfAbsent(i, new ExpansionTile(null));
        }
    }

    public void setSlot(int slot, Tile tile) {
        slotMap.put(slot, tile);
    }

    public Tile getSlot(int slot) {
        if (!slotMap.containsKey(slot)) {
            slotMap.put(slot, new ExpansionTile(null));
        }
        return slotMap.get(slot);
    }
}