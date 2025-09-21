package me.webhead1104.township.serializers;

import me.webhead1104.township.tiles.Tile;
import org.apache.commons.lang3.ClassUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.*;
import java.util.*;

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
        // Reverse to get super -> suborder
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

    private static boolean isTypeCompatible(Class<?> paramType, Object value) {
        if (value == null) return true; // null is compatible with any reference type
        Class<?> p = ClassUtils.primitiveToWrapper(paramType);
        return p.isInstance(value);
    }

    @Override
    public Tile deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.node(CLASS_KEY).getString() == null) {
            throw new SerializationException("Cannot deserialize a Tile without a class name!");
        }
        String className = "me.webhead1104.township.tiles." + node.node(CLASS_KEY).getString();

        try {
            Class<?> tileClass = Class.forName(className);

            if (!Tile.class.isAssignableFrom(tileClass)) {
                throw new SerializationException("Class " + className + " is not a subclass of Tile!");
            }

            ConfigurationNode propertiesNode = node.node(PROPERTIES_KEY);

            // Collect fields from the entire class hierarchy (super -> sub)
            List<Field> allFields = collectAllFields(tileClass);

            // Preserve insertion order to have deterministic behavior
            Map<String, Object> fieldValues = new LinkedHashMap<>();
            for (Field field : allFields) {
                String fieldName = field.getName();
                ConfigurationNode fieldNode = propertiesNode.node(fieldName);
                if (fieldNode.virtual()) {
                    continue;
                }
                Object value = fieldNode.get(field.getType());
                fieldValues.put(fieldName, value);
            }

            Tile tile = null;
            Constructor<?>[] constructors = tileClass.getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length == 0) {
                    continue;
                }

                // Strategy 1: Try to match by field name and type
                boolean canUseConstructor = true;
                Object[] args = new Object[paramTypes.length];
                Parameter[] parameters = constructor.getParameters();
                Set<String> used = new HashSet<>();

                for (int i = 0; i < paramTypes.length; i++) {
                    Parameter param = parameters[i];
                    String paramName = param.getName();
                    Class<?> pType = paramTypes[i];

                    // Strategy A: exact name match
                    if (fieldValues.containsKey(paramName)) {
                        Object v = fieldValues.get(paramName);
                        if (isTypeCompatible(pType, v)) {
                            args[i] = v;
                            used.add(paramName);
                            continue;
                        } else {
                            canUseConstructor = false;
                            break;
                        }
                    }

                    // Strategy B: Match by type only (for synthetic parameter names)
                    boolean matched = false;
                    for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                        if (used.contains(entry.getKey())) continue;
                        Object v = entry.getValue();
                        if (isTypeCompatible(pType, v)) {
                            args[i] = v;
                            used.add(entry.getKey());
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        canUseConstructor = false;
                        break;
                    }
                }

                if (canUseConstructor) {
                    constructor.setAccessible(true);
                    tile = (Tile) constructor.newInstance(args);
                    break;
                }
            }

            // Fallback: try the no-args constructor, then set fields reflectively
            if (tile == null) {
                try {
                    Constructor<?> constructor = tileClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    tile = (Tile) constructor.newInstance();
                    for (Field field : allFields) {
                        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                            continue;
                        }
                        String fieldName = field.getName();
                        if (fieldValues.containsKey(fieldName)) {
                            field.setAccessible(true);
                            field.set(tile, fieldValues.get(fieldName));
                        }
                    }
                } catch (NoSuchMethodException e) {
                    throw new SerializationException("Could not find a suitable constructor for class: " + className + ". Available field values: " + fieldValues);
                }
            }

            return tile;
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Could not find class: " + className);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SerializationException("Could not create instance of class: " + className + ". Cause: " + e.getCause());
        } catch (Exception e) {
            throw new SerializationException("Error deserializing Tile: " + e.getMessage());
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Tile obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Cannot serialize a null Tile!");
        }

        Class<?> tileClass = obj.getClass();
        node.node(CLASS_KEY).set(tileClass.getName().split("me.webhead1104.township.tiles.")[1]);

        ConfigurationNode propertiesNode = node.node(PROPERTIES_KEY);

        try {
            for (Field field : collectAllFields(tileClass)) {
                if (Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value != null) {
                    propertiesNode.node(fieldName).set(field.getGenericType(), value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new SerializationException("Error accessing fields of Tile: " + e.getMessage());
        } catch (Exception e) {
            throw new SerializationException("Error serializing Tile: " + e.getMessage());
        }
    }
}