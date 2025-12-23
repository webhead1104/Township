package me.webhead1104.towncraft.utils;

import me.webhead1104.towncraft.dataLoaders.ItemType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void testThing2() {
        assertEquals("Grass block", Utils.thing2("GRASS_BLOCK"));
        assertEquals("Stone", Utils.thing2("stone"));
        assertEquals("Diamond sword", Utils.thing2("DIAMOND_SWORD"));
    }

    @Test
    public void testFormat() {
        Instant now = Instant.now();
        Instant later = now.plus(Duration.ofDays(1).plusHours(2).plusMinutes(3).plusSeconds(4));
        assertEquals("1d 2h 3m 4s", Utils.format(now, later));

        later = now.plus(Duration.ofHours(5).plusMinutes(30));
        assertEquals("5h 30m", Utils.format(now, later));

        later = now.plus(Duration.ofMinutes(10));
        assertEquals("10m", Utils.format(now, later));

        later = now.plus(Duration.ofSeconds(45));
        assertEquals("45s", Utils.format(now, later));
    }

    @Test
    public void testCalculateAdjustedWeight() {
        int baseWeight = 100;

        assertEquals((int) (100 * 1.5), Utils.calculateAdjustedWeight(baseWeight, 10, 10));
        assertEquals((int) (100 * 1.5), Utils.calculateAdjustedWeight(baseWeight, 10, 15));

        assertEquals(100, Utils.calculateAdjustedWeight(baseWeight, 10, 16));
        assertEquals(100, Utils.calculateAdjustedWeight(baseWeight, 10, 20));

        assertEquals((int) (100 * 0.75), Utils.calculateAdjustedWeight(baseWeight, 10, 21));
        assertEquals((int) (100 * 0.75), Utils.calculateAdjustedWeight(baseWeight, 10, 30));

        assertEquals((int) (100 * 0.5), Utils.calculateAdjustedWeight(baseWeight, 10, 31));

        assertEquals(0, Utils.calculateAdjustedWeight(baseWeight, 10, 5));
    }

    @Test
    public void testSelectWeightedItem() {
        ItemType.Item item1 = new ItemType.Item() {
            @Override
            public int getBaseWeight() {
                return 100;
            }

            @Override
            public int getLevelNeeded() {
                return 1;
            }

            @Override
            public String getName() {
                return "Item 1";
            }
        };

        ItemType.Item item2 = new ItemType.Item() {
            @Override
            public int getBaseWeight() {
                return 100;
            }

            @Override
            public int getLevelNeeded() {
                return 10;
            }

            @Override
            public String getName() {
                return "Item 2";
            }
        };

        List<ItemType.Item> availableItems = List.of(item1, item2);
        List<ItemType.Item> alreadySelected = new ArrayList<>();

        ItemType.Item selected = Utils.selectWeightedItem(availableItems, alreadySelected, 1);
        assertEquals(item1, selected);

        selected = Utils.selectWeightedItem(availableItems, alreadySelected, 100);
        assertNotNull(selected);
        assertTrue(availableItems.contains(selected));
    }
}
