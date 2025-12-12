package me.webhead1104.towncraft.data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public record TileSize(int height, int width) {
    public static final TileSize SIZE_1X1 = new TileSize(1, 1);
    public static final TileSize SIZE_2X2 = new TileSize(2, 2);
    public static final TileSize SIZE_3X3 = new TileSize(3, 3);
    public static final TileSize SIZE_2X1 = new TileSize(1, 2);
    public static final TileSize SIZE_1X3 = new TileSize(1, 3);

    public List<Integer> toList(int startAt) {
        List<Integer> area = new ArrayList<>();
        int currentValue = startAt;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                area.add(currentValue++);
            }
            currentValue += 9 - width;
        }
        return area;
    }
}
