package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@AllArgsConstructor
public class WorldSection {
    private final Map<Integer, Tile> slotMap;
    private int sectionID;

    public static WorldSection createWorldPage(int sectionID) {
        Map<Integer, Tile> slotMap = new HashMap<>();
        AtomicInteger slot = new AtomicInteger(0);
        AtomicInteger times = new AtomicInteger(0);
        for (int i = 0; i < 54; i++) {
            slotMap.put(i, new Tile(WorldTileType.GRASS, null, null));
            if (sectionID != 27 && sectionID != 28 && sectionID != 35 && sectionID != 36) {
                Tile tile = new Tile(WorldTileType.EXPANSION, null, new Expansion(sectionID, slot.get(), 100, 20));
                slotMap.put(slot.getAndIncrement(), tile);
            }
            if (sectionID == 27) // this is plot testing
                slotMap.put(34, new Tile(WorldTileType.PLOT, new Plot(sectionID, 34, PlotType.NONE), null));
            if (sectionID == 27) {
                for (int a : Utils.thing("2x2", 0)) {
                    slotMap.put(a, new Tile(WorldTileType.BARN, null, null));
                }
//                AtomicInteger c = new AtomicInteger(2);
//                for (AnimalType animalType : AnimalType.values()) {
//                    for (int a : Utils.thing("2x2", c.get())) {
//                        slotMap.put(a, new Tile(animalType.getTileType(), null, null));
//                        c.addAndGet(2);
//                    }
//                }
//                for (FactoryType factoryType : FactoryType.values()) {
//                    for (int a : Utils.thing("2x2", c.get())) {
//                        slotMap.put(a, new Tile(factoryType.getTileType(), null, null));
//                        c.addAndGet(2);
//                    }
//                }
            }
            if (sectionID == 28) {
                for (int a : Utils.thing("1x3", 0)) {
                    slotMap.put(a, new Tile(WorldTileType.TRAIN, null, null));
                }
            }
        }
        return new WorldSection(slotMap, sectionID);
    }

    public void setSlot(int slot, Tile tile) {
        slotMap.put(slot, tile);
    }

    public void setSlot(int slot, WorldTileType slotType) {
        setSlot(slot, new Tile(slotType, null, null));
    }

    public Tile getSlot(int slot) {
        return slotMap.get(slot);
    }
}
