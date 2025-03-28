package me.webhead1104.township.data.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TileSize {
    SIZE_2X2(2, 2),
    SIZE_3X3(3, 3),
    SIZE_1X3(1, 3);
    private final int height;
    private final int width;

    TileSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

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
