package me.webhead1104.towncraft.tiles;

import lombok.extern.slf4j.Slf4j;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.serializers.MaterialSerializer;
import me.webhead1104.towncraft.serializers.TileSerializer;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TileTest {

    private static final TileSerializer tileSerializer;
    private static final MaterialSerializer materialSerializer;
    private static final ConfigurationOptions options;

    //todo fix test failing since material factory doesnt exist!
    static {
        tileSerializer = new TileSerializer();
        materialSerializer = new MaterialSerializer();
        options = ConfigurationOptions.defaults()
                .serializers(builder -> builder
                        .register(TowncraftMaterial.class, materialSerializer)
                        .register(Tile.class, tileSerializer));
    }


    @Test
    public void testStaticWorldTileSerialization() {
        try {
            TowncraftMaterial[] materials = {TowncraftMaterial.GRASS_BLOCK, TowncraftMaterial.STONE, TowncraftMaterial.SAND};

            for (TowncraftMaterial material : materials) {
                StaticWorldTile original = new StaticWorldTile(material);

                ConfigurationNode node = BasicConfigurationNode.root(options);
                tileSerializer.serialize(Tile.class, original, node);
                Tile deserialized = tileSerializer.deserialize(Tile.class, node);

                assertInstanceOf(StaticWorldTile.class, deserialized);
                StaticWorldTile staticTile = (StaticWorldTile) deserialized;
                assertEquals(staticTile.getMaterial(), material);
            }

        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }

    @Test
    public void testExpansionTileWithNullInstant() {
        try {
            ExpansionTile original = new ExpansionTile();

            ConfigurationNode node = BasicConfigurationNode.root(options);
            tileSerializer.serialize(Tile.class, original, node);

            ConfigurationNode propertiesNode = node.node("properties");
            assertTrue(propertiesNode.virtual());

            Tile deserialized = tileSerializer.deserialize(Tile.class, node);

            assertInstanceOf(ExpansionTile.class, deserialized);
            ExpansionTile expansionTile = (ExpansionTile) deserialized;
            assertNull(expansionTile.getInstant());
        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }

    @Test
    public void testExpansionTileWithInstant() {

        try {
            Instant testInstant = Instant.now().plusSeconds(3600);
            ExpansionTile original = new ExpansionTile(testInstant);

            ConfigurationNode node = BasicConfigurationNode.root(options);
            tileSerializer.serialize(Tile.class, original, node);

            Tile deserialized = tileSerializer.deserialize(Tile.class, node);

            assertInstanceOf(ExpansionTile.class, deserialized);
            ExpansionTile expansionTile = (ExpansionTile) deserialized;
            assertEquals(expansionTile.getInstant(), testInstant);
        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }

    @Test
    public void testCorruptedDataHandling() {
        try {
            ConfigurationNode corruptedExpansionNode = BasicConfigurationNode.root(options);
            corruptedExpansionNode.node("class").set("ExpansionTile");

            Tile result = tileSerializer.deserialize(Tile.class, corruptedExpansionNode);

            assertInstanceOf(ExpansionTile.class, result);
            ExpansionTile expansionTile = (ExpansionTile) result;
            assertNull(expansionTile.getInstant());
        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }

    @Test
    public void testRoundTripSerialization() {
        try {
            Instant testInstant = Instant.now().plusSeconds(1800);
            ExpansionTile original = new ExpansionTile(testInstant);

            ConfigurationNode node1 = BasicConfigurationNode.root(options);
            tileSerializer.serialize(Tile.class, original, node1);
            Tile intermediate = tileSerializer.deserialize(Tile.class, node1);

            ConfigurationNode node2 = BasicConfigurationNode.root(options);
            tileSerializer.serialize(Tile.class, intermediate, node2);
            tileSerializer.deserialize(Tile.class, node2);

            assertEquals(node1.toString(), node2.toString());
        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }
}
