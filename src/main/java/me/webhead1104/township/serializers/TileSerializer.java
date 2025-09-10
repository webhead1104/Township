package me.webhead1104.township.serializers;

import me.webhead1104.township.tiles.Tile;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class TileSerializer implements TypeSerializer<Tile> {

    private static final String CLASS_KEY = "class";
    private static final String PROPERTIES_KEY = "properties";

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
            Field[] fields = tileClass.getDeclaredFields();
            Map<String, Object> fieldValues = new HashMap<>();
            for (Field field : fields) {
                String fieldName = field.getName();
                ConfigurationNode fieldNode = propertiesNode.node(fieldName);
                if (fieldNode.virtual()) {
                    continue;
                }
                Object value = fieldNode.get(field.getGenericType());
                fieldValues.put(fieldName, value);
            }

            Tile tile = null;
            Constructor<?>[] constructors = tileClass.getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length == 0) {
                    continue;
                }

                // Strategy 1: Try to match by field name
                boolean canUseConstructor = true;
                Object[] args = new Object[paramTypes.length];

                Parameter[] parameters = constructor.getParameters();

                for (int i = 0; i < paramTypes.length; i++) {
                    Parameter param = parameters[i];
                    String paramName = param.getName();

                    if (fieldValues.containsKey(paramName)) {
                        args[i] = fieldValues.get(paramName);
                    }
                    // Check if the parameter name follows the "argN" pattern (synthetic name)
                    else if (paramName.matches("arg\\d+")) {
                        // If we have synthetic parameter names (arg0, arg1, etc.), try to match by position
                        // This assumes the constructor parameters are in the same order as the fields
                        if (i < fieldValues.size()) {
                            String fieldName = (String) fieldValues.keySet().toArray()[i];
                            Object value = fieldValues.get(fieldName);
                            if (paramTypes[i].isInstance(value)) {
                                args[i] = value;
                            } else {
                                canUseConstructor = false;
                                break;
                            }
                        } else {
                            canUseConstructor = false;
                            break;
                        }
                    } else {
                        canUseConstructor = false;
                        break;
                    }
                }
                if (canUseConstructor) {
                    constructor.setAccessible(true);
                    tile = (Tile) constructor.newInstance(args);
                    break;
                }

                // Strategy 2: If the constructor has exactly one parameter, and we have exactly one field value
                if (paramTypes.length == 1 && fieldValues.size() == 1) {
                    Object value = fieldValues.values().iterator().next();
                    if (paramTypes[0].isInstance(value)) {
                        constructor.setAccessible(true);
                        tile = (Tile) constructor.newInstance(value);
                        break;
                    }
                }
            }

            // Strategy 3: If no suitable constructor with parameters, try the no-args constructor
            if (tile == null) {
                try {
                    Constructor<?> constructor = tileClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    tile = (Tile) constructor.newInstance();
                    for (Field field : fields) {
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
                    throw new SerializationException("Could not find a suitable constructor for class: " + className);
                }
            }

            return tile;
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Could not find class: " + className);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SerializationException("Could not create instance of class: " + className);
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
        node.node(CLASS_KEY).set(tileClass.getName().split("me.webhead1104.township.tiles.tiles.")[1]);

        ConfigurationNode propertiesNode = node.node(PROPERTIES_KEY);

        try {
            for (Field field : tileClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
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