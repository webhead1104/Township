package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerTestImpl;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class TowncraftInventoryTestImplTest {
    private static final Component COMPONENT = Msg.format("hello world");
    private TowncraftInventoryTestImpl towncraftInventoryImpl;

    @BeforeEach
    void setUp() {
        towncraftInventoryImpl = new TowncraftInventoryTestImpl(TowncraftInventoryType.CHEST, 54, COMPONENT);
    }

    @Test
    void testNullInventoryType() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new TowncraftInventoryTestImpl(null, 54, COMPONENT));
        assertEquals("inventoryType cannot be null", exception.getMessage());
    }

    @Test
    void testInvalidInventorySize() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new TowncraftInventoryTestImpl(TowncraftInventoryType.CHEST, -1, COMPONENT));
        assertEquals("Inventory size has to be > 0", exception.getMessage());
    }

    @Test
    void testNullTitle() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new TowncraftInventoryTestImpl(TowncraftInventoryType.CHEST, 54, null));
        assertEquals("Title cannot be null", exception.getMessage());
    }

    @Test
    void testSizeDefaultValue() {
        assertEquals(54, towncraftInventoryImpl.getSize());
    }

    @Test
    void testInventoryTypeDefaultValue() {
        assertEquals(TowncraftInventoryType.CHEST, towncraftInventoryImpl.getType());
    }

    @Test
    void testGetPlatformThrows() {
        assertThrows(UnsupportedOperationException.class, () -> towncraftInventoryImpl.getPlatform());
    }

    @Nested
    class Title {
        @Test
        void testDefaultValue() {
            assertEquals(COMPONENT, towncraftInventoryImpl.getTitle());
        }

        @Test
        void testSetTitle() {
            Component component = Msg.format("new hello world");
            towncraftInventoryImpl.setTitle(component);
            assertEquals(component, towncraftInventoryImpl.getTitle());
        }
    }

    @Nested
    class Viewers {
        @Test
        void testDefaultValue() {
            assertEquals(new ArrayList<>(), towncraftInventoryImpl.getViewers());
        }

        @Test
        void testOpen() {
            TowncraftPlayer player = new TowncraftPlayerTestImpl(UUID.randomUUID());
            player.openInventory(towncraftInventoryImpl);
            assertEquals(List.of(player), towncraftInventoryImpl.getViewers());
        }
    }

    @Nested
    class Item {
        @Test
        void testDefaultValue() {
            assertEquals(TowncraftItemStack.empty(), towncraftInventoryImpl.getItem(0));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 9, 18, 27, 36, 45, 54})
        void testSetItem(int slot) {
            TowncraftItemStack itemStack = TowncraftItemStack.empty();
            towncraftInventoryImpl.setItem(slot, itemStack);
            assertEquals(itemStack, towncraftInventoryImpl.getItem(slot));
        }


        @ParameterizedTest
        @ValueSource(ints = {-1, 55})
        void testSetInvalidValues(int value) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> towncraftInventoryImpl.setItem(value, TowncraftItemStack.empty()));
            assertEquals("Slot must be between 0 and 54", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 55})
        void testGetInvalidValues(int value) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> towncraftInventoryImpl.getItem(value));
            assertEquals("Slot must be between 0 and 54", exception.getMessage());
        }
    }

    @Nested
    class Contents {
        @Test
        void testDefaultValue() {
            TowncraftItemStack[] expected = new TowncraftItemStack[55];
            Arrays.fill(expected, TowncraftItemStack.empty());
            assertArrayEquals(expected, towncraftInventoryImpl.getContents());
        }

        @Test
        void testSetContents() {
            TowncraftItemStack[] expected = new TowncraftItemStack[55];
            Arrays.fill(expected, TowncraftItemStack.empty());
            expected[0] = TowncraftItemStack.of(TowncraftMaterial.STONE);
            towncraftInventoryImpl.setContents(expected);
            assertArrayEquals(expected, towncraftInventoryImpl.getContents());
        }
    }

    @Nested
    class Clear {
        @Test
        void testClear() {
            towncraftInventoryImpl.setItem(0, TowncraftItemStack.of(TowncraftMaterial.STONE));
            towncraftInventoryImpl.clear();
            assertEquals(towncraftInventoryImpl.getItem(0), TowncraftItemStack.empty());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 9, 18, 27, 36, 45, 54})
        void testClear(int value) {
            towncraftInventoryImpl.setItem(value, TowncraftItemStack.of(TowncraftMaterial.STONE));
            towncraftInventoryImpl.clear(value);
            assertEquals(towncraftInventoryImpl.getItem(value), TowncraftItemStack.empty());
        }
    }
}