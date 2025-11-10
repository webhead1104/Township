package me.webhead1104.towncraft.serializers;

import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TowncraftSerializer<T> implements TypeSerializer<T> {

    @SuppressWarnings("unchecked")
    public Class<T> getTargetClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType paramType) {
            Type targetType = paramType.getActualTypeArguments()[0];
            if (targetType instanceof Class<?>) {
                return (Class<T>) targetType;
            }
        }
        throw new IllegalStateException("Could not determine target class for serializer: " + getClass().getName());
    }
}
