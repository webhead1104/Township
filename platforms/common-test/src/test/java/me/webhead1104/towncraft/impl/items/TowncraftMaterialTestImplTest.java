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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class TowncraftMaterialTestImplTest {
    private TowncraftMaterialTestImpl towncraftMaterialTestImpl;

    @BeforeEach
    void setUp() {
        towncraftMaterialTestImpl = new TowncraftMaterialTestImpl(key("stone"));
    }

    @Test
    void testGetPlatformThrows() {
        assertThrows(UnsupportedOperationException.class, () -> towncraftMaterialTestImpl.getPlatform());
    }

    @Nested
    class Type {
        @Test
        void getKey() {
            assertDoesNotThrow(() -> towncraftMaterialTestImpl.key());
        }

        @Test
        void key() {
            TowncraftMaterialTestImpl material =
                    assertDoesNotThrow(() -> new TowncraftMaterialTestImpl(Key.key("stone")));
            assertEquals(material.key(), towncraftMaterialTestImpl.key());
        }

        @Test
        void nullKey() {
            NullPointerException exception =
                    assertThrows(NullPointerException.class, () -> new TowncraftMaterialTestImpl(null));
            assertEquals("key cannot be null", exception.getMessage());
        }
    }
}
