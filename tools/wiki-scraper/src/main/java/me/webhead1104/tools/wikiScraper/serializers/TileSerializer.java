package me.webhead1104.tools.wikiScraper.serializers;

import me.webhead1104.tools.wikiScraper.model.tile.Tile;
import org.apache.commons.lang3.NotImplementedException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileSerializer implements TypeSerializer<Tile> {

    private static final String CLASS_KEY = "class";
    private static final String PROPERTIES_KEY = "properties";

    private static List<Field> collectAllFields(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && Tile.class.isAssignableFrom(current)) {
            hierarchy.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(hierarchy);

        List<Field> result = new ArrayList<>();
        for (Class<?> c : hierarchy) {
            for (Field f : c.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                result.add(f);
            }
        }
        return result;
    }

    @Override
    public Tile deserialize(Type type, ConfigurationNode node) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public void serialize(Type type, @Nullable Tile obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Cannot serialize a null Tile!");
        }

        Class<?> tileClass = obj.getClass();
        node.node(CLASS_KEY).set(tileClass.getName().split("me.webhead1104.tools.wikiScraper.model.tile.")[1]);

        ConfigurationNode propertiesNode = node.node(PROPERTIES_KEY);

        try {
            boolean hasNonNullFields = false;
            for (Field field : collectAllFields(tileClass)) {
                if (Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value != null) {
                    propertiesNode.node(fieldName).set(field.getGenericType(), value);
                    hasNonNullFields = true;
                }
            }

            if (!hasNonNullFields) {
                node.removeChild(PROPERTIES_KEY);
            }
        } catch (IllegalAccessException e) {
            throw new SerializationException("Error accessing fields of Tile: " + e.getMessage());
        } catch (Exception e) {
            throw new SerializationException("Error serializing Tile: " + e.getMessage());
        }
    }
}