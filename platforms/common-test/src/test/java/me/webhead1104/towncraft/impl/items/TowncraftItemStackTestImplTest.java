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
package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class TowncraftItemStackTestImplTest {
    private static final Component COMPONENT = Msg.format("hello world");
    private TowncraftItemStackTestImpl towncraftItemStackImpl;

    @BeforeEach
    void setUp() {
        towncraftItemStackImpl = new TowncraftItemStackTestImpl(TowncraftMaterial.STONE);
    }

    @Test
    void testNullMaterial() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new TowncraftItemStackTestImpl(null));
        assertEquals("material cannot be null", exception.getMessage());
    }

    @Test
    void testOverrideNameColor() {
        assertDoesNotThrow(() -> towncraftItemStackImpl.overrideNameColor());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testHideTooltip(boolean value) {
        assertDoesNotThrow(() -> towncraftItemStackImpl.hideTooltip(value));
    }

    @Test
    void testIsEmpty() {
        towncraftItemStackImpl.setMaterial(TowncraftMaterial.AIR);
        assertTrue(towncraftItemStackImpl.isEmpty());
    }

    @Test
    void testToPlatformThrows() {
        assertThrows(UnsupportedOperationException.class, () -> towncraftItemStackImpl.toPlatform());
    }

    @Nested
    class Name {
        @Test
        void name() {
            assertDoesNotThrow(() -> towncraftItemStackImpl.setName(COMPONENT));
        }

        @Test
        void nullName() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> towncraftItemStackImpl.setName(null));
            assertEquals("name cannot be null", exception.getMessage());
        }
    }

    @Nested
    class Lore {
        @Test
        void lore() {
            assertDoesNotThrow(() -> towncraftItemStackImpl.setLore(List.of(COMPONENT)));
        }

        @Test
        void nullLore() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> towncraftItemStackImpl.setLore((List<Component>) null));
            assertEquals("lore cannot be null", exception.getMessage());
        }
    }

    @Nested
    class Similar {
        @Test
        void similarTrue() {
            TowncraftItemStack itemStack = new TowncraftItemStackTestImpl(TowncraftMaterial.STONE);
            assertTrue(towncraftItemStackImpl.isSimilar(itemStack));
        }

        @Test
        void similarFalse() {
            TowncraftItemStack itemStack = new TowncraftItemStackTestImpl(TowncraftMaterial.ANDESITE);
            assertFalse(towncraftItemStackImpl.isSimilar(itemStack));
        }

        @Test
        void similarNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> towncraftItemStackImpl.isSimilar(null));
            assertEquals("other cannot be null", exception.getMessage());
        }
    }

    @Nested
    class Material {
        @Test
        void defaultMaterial() {
            assertEquals(TowncraftMaterial.STONE, towncraftItemStackImpl.getMaterial());
        }

        @Test
        void setMaterial() {
            towncraftItemStackImpl.setMaterial(TowncraftMaterial.ANDESITE);
            assertEquals(TowncraftMaterial.ANDESITE, towncraftItemStackImpl.getMaterial());
        }

        @Test
        void nullMaterial() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> towncraftItemStackImpl.setMaterial(null));
            assertEquals("material cannot be null", exception.getMessage());
        }
    }

    @Nested
    class PlayerHeadTexture {
        @Test
        void setPlayerHeadTexture() {
            assertDoesNotThrow(() -> towncraftItemStackImpl.setPlayerHeadTexture("the texture"));
        }

        @Test
        void nullPlayerHeadTexture() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> towncraftItemStackImpl.setPlayerHeadTexture(null));
            assertEquals("texture cannot be null", exception.getMessage());
        }
    }
}
