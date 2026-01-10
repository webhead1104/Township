package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.impl.items.TowncraftInventoryTestImpl;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryTestImpl;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import me.webhead1104.towncraft.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class TowncraftPlayerTestImplTest {
    private TowncraftPlayerTestImpl towncraftPlayerTestImpl;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        towncraftPlayerTestImpl = new TowncraftPlayerTestImpl(uuid);
    }

    @Test
    void testDefaultName() {
        assertEquals("Webhead1104", towncraftPlayerTestImpl.getName());
    }

    @Test
    void testDefaultPlayerInventory() {
        assertEquals(new TowncraftPlayerInventoryTestImpl(), towncraftPlayerTestImpl.getInventory());
    }

    @Test
    void testSendMessageNotThrowing() {
        assertDoesNotThrow(() -> towncraftPlayerTestImpl.sendMessage(Msg.format("hello world")));
    }

    @Test
    void testSendMessageNull() {
        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> towncraftPlayerTestImpl.sendMessage(null));
        assertEquals("message cannot be null", exception.getMessage());
    }

    @Nested
    class Uuid {
        @Test
        void defaultUUID() {
            assertEquals(uuid, towncraftPlayerTestImpl.getUUID());
        }

        @Test
        void uuid() {
            assertDoesNotThrow(() -> new TowncraftPlayerTestImpl(uuid));
        }

        @Test
        void nullUuid() {
            NullPointerException exception =
                    assertThrows(NullPointerException.class, () -> new TowncraftPlayerTestImpl(null));
            assertEquals("uuid cannot be null", exception.getMessage());
        }
    }

    @Nested
    class OpenInventory {
        @Test
        void defaultOpenInventory() {
            assertNull(towncraftPlayerTestImpl.getOpenInventory());
        }

        @Test
        void openInventory() {
            TowncraftInventoryTestImpl inventory =
                    new TowncraftInventoryTestImpl(TowncraftInventoryType.CHEST, 54, Msg.format("hello world"));

            towncraftPlayerTestImpl.openInventory(inventory);
            assertEquals(inventory, towncraftPlayerTestImpl.getOpenInventory());

            assertTrue(inventory.getViewers().contains(towncraftPlayerTestImpl));

            towncraftPlayerTestImpl.closeInventory();
            assertNull(towncraftPlayerTestImpl.getOpenInventory());
            assertFalse(inventory.getViewers().contains(towncraftPlayerTestImpl));


            towncraftPlayerTestImpl.openInventory(inventory);
            assertEquals(inventory, towncraftPlayerTestImpl.getOpenInventory());
            assertTrue(inventory.getViewers().contains(towncraftPlayerTestImpl));

            towncraftPlayerTestImpl.closeInventory();

            assertNull(towncraftPlayerTestImpl.getOpenInventory());
            assertFalse(inventory.getViewers().contains(towncraftPlayerTestImpl));
        }

        @Test
        void closeInventory_WhenOpen() {
            TowncraftInventoryTestImpl inventory =
                    new TowncraftInventoryTestImpl(TowncraftInventoryType.CHEST, 54, Msg.format("hello world"));

            towncraftPlayerTestImpl.openInventory(inventory);
            towncraftPlayerTestImpl.closeInventory();
            assertNull(towncraftPlayerTestImpl.getOpenInventory());
        }

        @Test
        void closeInventory_WhenAlreadyClosed() {
            towncraftPlayerTestImpl.closeInventory();
            assertNull(towncraftPlayerTestImpl.getOpenInventory());
        }

        @Test
        void nullOpenInventory() {
            NullPointerException exception =
                    assertThrows(NullPointerException.class, () -> new TowncraftPlayerTestImpl(null));
            assertEquals("uuid cannot be null", exception.getMessage());
        }
    }

    @Nested
    class ItemOnCursor {
        @Test
        void defaultItemOnCursor() {
            assertEquals(TowncraftItemStack.empty(), towncraftPlayerTestImpl.getItemOnCursor());
        }

        @Test
        void getItemOnCursor() {
            assertDoesNotThrow(() -> towncraftPlayerTestImpl.getItemOnCursor());
        }

        @Test
        void setItemOnCursor() {
            assertDoesNotThrow(() -> towncraftPlayerTestImpl.setItemOnCursor(TowncraftItemStack.of(TowncraftMaterial.STONE)));
        }

        @Test
        void setItemOnCursorNull() {
            NullPointerException exception =
                    assertThrows(NullPointerException.class, () -> towncraftPlayerTestImpl.setItemOnCursor(null));

            assertEquals("itemStack cannot be null", exception.getMessage());
        }
    }
}