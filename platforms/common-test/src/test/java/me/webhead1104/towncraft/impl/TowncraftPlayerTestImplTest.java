/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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