package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class World {
    private final Map<Integer, WorldSection> worldMap = new HashMap<>();

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
