package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public class WorldSection {
    public static final @NotNull Codec<WorldSection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(TownshipCodecs.INT, Tile.CODEC).fieldOf("slotMap").forGetter(WorldSection::getSlotMap),
            Codec.INT.fieldOf("section").forGetter(WorldSection::getSection)
    ).apply(instance, WorldSection::new));
    private final Map<Integer, Tile> slotMap = new HashMap<>();
    private int section;

    public WorldSection(Map<Integer, Tile> slotMap, int section) {
        this.slotMap.putAll(slotMap);
        this.section = section;
    }

    public WorldSection(int section) {
        AtomicInteger slot = new AtomicInteger(0);
        for (int i = 0; i < 54; i++) {
            slotMap.put(i, new Tile(WorldTileType.GRASS, null, null));
            if (section != 27 && section != 28 && section != 35 && section != 36) {
                Tile tile = new Tile(WorldTileType.EXPANSION, null, new Expansion(section, slot.get(), 100, 20));
                slotMap.put(slot.getAndIncrement(), tile);
            }
            if (section == 27)
                slotMap.put(34, new Tile(WorldTileType.PLOT, new Plot(section, 34, PlotType.NONE), null));
            if (section == 27) {
                for (int a : Utils.thing("2x2", 0)) {
                    slotMap.put(a, new Tile(WorldTileType.BARN, null, null));
                }
            }
            if (section == 28) {
                for (int a : Utils.thing("1x3", 0)) {
                    slotMap.put(a, new Tile(WorldTileType.TRAIN, null, null));
                }
            }
        }
        this.section = section;
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
