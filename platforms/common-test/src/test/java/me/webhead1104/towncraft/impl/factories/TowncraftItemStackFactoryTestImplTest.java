package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftItemStackFactory;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackTestImpl;
import me.webhead1104.towncraft.impl.items.TowncraftMaterialTestImpl;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;

class TowncraftItemStackFactoryTestImplTest {
    private final TowncraftItemStackFactoryTestImpl factory = new TowncraftItemStackFactoryTestImpl();

    @Test
    void testGet0() {
        TowncraftMaterial material = new TowncraftMaterialTestImpl(key("stone"));
        Assertions.assertEquals(new TowncraftItemStackTestImpl(material), factory.get0(material));
    }

    @Test
    void testGet_throughStaticMethod() {
        TowncraftMaterial material = new TowncraftMaterialTestImpl(key("stone"));
        Assertions.assertEquals(new TowncraftItemStackTestImpl(material), TowncraftItemStackFactory.get(material));
    }
}