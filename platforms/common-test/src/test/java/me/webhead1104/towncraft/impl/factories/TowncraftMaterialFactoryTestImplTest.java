package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftMaterialFactory;
import me.webhead1104.towncraft.impl.items.TowncraftMaterialTestImpl;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;

class TowncraftMaterialFactoryTestImplTest {
    private static final Key KEY = key("stone");
    private final TowncraftMaterialFactoryTestImpl factory = new TowncraftMaterialFactoryTestImpl();

    @Test
    void testGet0() {
        TowncraftMaterial result = factory.get0(KEY);
        Assertions.assertEquals(new TowncraftMaterialTestImpl(KEY), result);
    }

    @Test
    void testGet_throughStaticMethod() {
        TowncraftMaterial result = TowncraftMaterialFactory.get(KEY);
        Assertions.assertEquals(new TowncraftMaterialTestImpl(KEY), result);
    }
}