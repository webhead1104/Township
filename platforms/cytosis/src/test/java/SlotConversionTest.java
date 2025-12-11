import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlotConversionTest {

    // The method we're testing
    private int convertSlot(boolean isTopInventory, int rawSlot, int numInTop) {
        if (isTopInventory) {
            return rawSlot; // 0 to numInTop-1
        }

        // Clicking in player inventory
        // rawSlot is 0-35 from Minestom

        // Window layout:
        // numInTop to numInTop+26: Main inventory (3 rows, 27 slots)
        // numInTop+27 to numInTop+35: Hotbar (1 row, 9 slots)

        // Minestom player inventory:
        // 0-8: Hotbar
        // 9-35: Main inventory

        if (rawSlot >= 0 && rawSlot < 9) {
            // Hotbar (0-8) -> window slots 81-89
            return numInTop + 27 + rawSlot;
        } else if (rawSlot >= 9 && rawSlot < 36) {
            // Main inventory (9-35) -> window slots 54-80
            return numInTop + (rawSlot - 9);
        }

        return rawSlot;
    }

    @Test
    public void test() {
        assertEquals(81, convertSlot(false, 0, 54));
    }

    @Test
    public void testChestInventory() {
        int numInTop = 54; // 6 row chest

        // Test top inventory (chest)
        assertEquals(0, convertSlot(true, 0, numInTop),
                "Top-left of chest should be 0");
        assertEquals(53, convertSlot(true, 53, numInTop),
                "Bottom-right of chest should be 53");
        assertEquals(26, convertSlot(true, 26, numInTop),
                "Middle slot of chest should be 26");
    }

    @Test
    public void testPlayerMainInventory() {
        int numInTop = 55;

        // Test player main inventory (top 3 rows)
        assertEquals(54, convertSlot(false, 9, numInTop),
                "Top-left of player inventory should be 54");
        assertEquals(62, convertSlot(false, 17, numInTop),
                "Top-right of player inventory should be 62");
        assertEquals(80, convertSlot(false, 35, numInTop),
                "Bottom-right of main inventory (before hotbar) should be 80");
    }

    @Test
    public void testPlayerHotbar() {
        int numInTop = 54;

        // Test player hotbar (bottom row)
        assertEquals(81, convertSlot(false, 0, numInTop),
                "Bottom-left of player (hotbar left) should be 81");
        assertEquals(89, convertSlot(false, 8, numInTop),
                "Bottom-right of player (hotbar right) should be 89");
        assertEquals(85, convertSlot(false, 4, numInTop),
                "Middle of hotbar should be 85");
    }

    @Test
    public void testAllPlayerSlots() {
        int numInTop = 54;

        System.out.println("=== Player Inventory Mapping ===");

        // Hotbar (Minestom 0-8 -> Window 81-89)
        System.out.println("\nHotbar:");
        for (int i = 0; i < 9; i++) {
            int windowSlot = convertSlot(false, i, numInTop);
            System.out.println("Minestom slot " + i + " -> Window slot " + windowSlot);
            assertEquals(81 + i, windowSlot);
        }

        // Main inventory (Minestom 9-35 -> Window 54-80)
        System.out.println("\nMain Inventory:");
        for (int i = 9; i < 36; i++) {
            int windowSlot = convertSlot(false, i, numInTop);
            System.out.println("Minestom slot " + i + " -> Window slot " + windowSlot);
            assertEquals(54 + (i - 9), windowSlot);
        }
    }

    @Test
    public void testDifferentChestSizes() {
        // Test with 3 row chest (27 slots)
        int numInTop = 27;
        assertEquals(27, convertSlot(false, 9, numInTop),
                "Top-left of player with 3-row chest should be 27");
        assertEquals(54, convertSlot(false, 0, numInTop),
                "Hotbar left with 3-row chest should be 54");
        assertEquals(62, convertSlot(false, 8, numInTop),
                "Hotbar right with 3-row chest should be 62");
    }
}