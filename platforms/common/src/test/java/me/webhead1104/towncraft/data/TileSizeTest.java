package me.webhead1104.towncraft.data;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileSizeTest {

    @Test
    public void testToList1x1() {
        TileSize size = TileSize.SIZE_1X1;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0), area);

        area = size.toList(10);
        assertEquals(List.of(10), area);
    }

    @Test
    public void testToList2x2() {
        TileSize size = TileSize.SIZE_2X2;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 9, 10), area);
    }

    @Test
    public void testToList2x1() {
        TileSize size = TileSize.SIZE_2X1;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1), area);
    }

    @Test
    public void testToList1x3() {
        TileSize size = TileSize.SIZE_1X3;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 2), area);
    }

    @Test
    public void testToList3x3() {
        TileSize size = TileSize.SIZE_3X3;
        List<Integer> area = size.toList(0);
        assertEquals(List.of(0, 1, 2, 9, 10, 11, 18, 19, 20), area);
    }
}
