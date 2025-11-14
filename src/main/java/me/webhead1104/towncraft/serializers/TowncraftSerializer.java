package me.webhead1104.towncraft.serializers;

import org.spongepowered.configurate.serialize.TypeSerializer;

public abstract class TowncraftSerializer<T> implements TypeSerializer<T> {

    public abstract Class<T> getType();
}
