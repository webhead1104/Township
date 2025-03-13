package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class World {
    public static final @NotNull Codec<World> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(TownshipCodecs.INT, WorldSection.CODEC).fieldOf("worldMap").forGetter(World::getWorldMap)
    ).apply(instance, World::new));
    private final Map<Integer, WorldSection> worldMap = new HashMap<>();

    public World(Map<Integer, WorldSection> worldMap) {
        this.worldMap.putAll(worldMap);
    }

    public World() {
        //8x8
        for (int i = 0; i < 64; i++) {
            worldMap.put(i, new WorldSection(i));
        }
    }

    public WorldSection getSection(int section) {
        return worldMap.get(section);
    }

    public void setSection(int page, WorldSection worldSection) {
        worldMap.put(page, worldSection);
    }
}
