/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
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
package me.webhead1104.towncraft.serializers;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.tiles.ExpansionTile;
import me.webhead1104.towncraft.tiles.Tile;
import org.apache.commons.lang3.ClassUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.*;
import java.util.*;

@Keep
public class TileSerializer extends TowncraftSerializer<Tile> {

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

    private static boolean isTypeCompatible(Class<?> paramType, Object value) {
        if (value == null) return true;
        Class<?> p = ClassUtils.primitiveToWrapper(paramType);
        return p.isInstance(value);
    }

    @Override
    public Tile deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual() || node.empty()) {
            return new ExpansionTile();
        }

        if (node.node(CLASS_KEY).getString() == null) {
            throw new SerializationException("Cannot deserialize a Tile without a class name!");
        }
        String className = "me.webhead1104.towncraft.tiles." + node.node(CLASS_KEY).getString();

        try {
            Class<?> tileClass = Class.forName(className);

            if (!Tile.class.isAssignableFrom(tileClass)) {
                throw new SerializationException("Class " + className + " is not a subclass of Tile!");
            }

            ConfigurationNode propertiesNode = node.node(PROPERTIES_KEY);

            List<Field> allFields = collectAllFields(tileClass);
            // Collect only fields declared on the concrete class (not inherited)
            Set<String> declaredFieldNames = new HashSet<>();
            for (Field f : tileClass.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    declaredFieldNames.add(f.getName());
                }
            }

            Map<String, Object> fieldValues = new LinkedHashMap<>();
            if (!propertiesNode.virtual()) {
                for (Field field : allFields) {
                    String fieldName = field.getName();
                    ConfigurationNode fieldNode = propertiesNode.node(fieldName);
                    if (!fieldNode.virtual()) {
                        Object value = fieldNode.get(field.getType());
                        fieldValues.put(fieldName, value);
                    }
                }
            }

            Tile tile = null;
            Constructor<?>[] constructors = tileClass.getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length == 0) {
                    continue;
                }

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

                    // Strategy B: Match by type only
                    // Prefer matches for fields declared on the concrete class (avoid inherited clashes)
                    boolean matched = false;
                    // First pass: only declared-on-class fields
                    for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                        if (used.contains(entry.getKey())) continue;
                        if (!declaredFieldNames.contains(entry.getKey())) continue;
                        Object v = entry.getValue();
                        if (isTypeCompatible(pType, v)) {
                            args[i] = v;
                            used.add(entry.getKey());
                            matched = true;
                            break;
                        }
                    }
                    // Second pass: any remaining fields
                    if (!matched) {
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
                    }

                    if (!matched) {
                        if (!pType.isPrimitive()) {
                            args[i] = null;
                        } else {
                            canUseConstructor = false;
                            break;
                        }
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
                        field.setAccessible(true);

                        if (fieldValues.containsKey(fieldName)) {
                            field.set(tile, fieldValues.get(fieldName));
                        } else {
                            // Explicitly set to null if not in properties
                            if (!field.getType().isPrimitive()) {
                                field.set(tile, null);
                            }
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
            throw new SerializationException(Tile.class, "Could not create instance of class: " + className + ". Cause: " + e.getCause(), e);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing Tile: " + e.getMessage());
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Tile obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Cannot serialize a null Tile!");
        }

        if (obj instanceof ExpansionTile expansionTile && expansionTile.getInstant() == null) {
            return;
        }

        Class<?> tileClass = obj.getClass();
        node.node(CLASS_KEY).set(tileClass.getName().split("me.webhead1104.towncraft.tiles.")[1]);

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

            // If no non-null fields were serialized, remove the properties node
            if (!hasNonNullFields) {
                node.removeChild(PROPERTIES_KEY);
            }
        } catch (IllegalAccessException e) {
            throw new SerializationException("Error accessing fields of Tile: " + e.getMessage());
        } catch (Exception e) {
            throw new SerializationException("Error serializing Tile: " + e.getMessage());
        }
    }

    @Override
    public Class<Tile> getType() {
        return Tile.class;
    }
}