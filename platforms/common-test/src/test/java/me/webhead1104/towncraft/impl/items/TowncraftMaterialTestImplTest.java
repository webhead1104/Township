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
