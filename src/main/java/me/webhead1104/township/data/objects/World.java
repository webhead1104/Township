package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class World {
    private final Map<Integer, WorldSection> worldMap;

    public static World createWorld() {
        Map<Integer, WorldSection> map = new HashMap<>();
        //8x8
        for (int i = 0; i < 64; i++) {
            map.put(i, WorldSection.createWorldPage(i));
        }
        return new World(map);
    }

    public WorldSection getSection(int section) {
        return worldMap.get(section);
    }

    public void setSection(int page, WorldSection worldSection) {
        worldMap.put(page, worldSection);
    }
}
