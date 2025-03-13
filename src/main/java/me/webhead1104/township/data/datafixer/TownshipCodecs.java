package me.webhead1104.township.data.datafixer;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public final class TownshipCodecs {
    public static final @NotNull Codec<UUID> UUID = Codec.STRING.xmap(java.util.UUID::fromString, java.util.UUID::toString);
    public static final @NotNull Codec<Instant> INSTANT = Codec.STRING.xmap(Instant::parse, Instant::toString);
    public static final @NotNull Codec<Integer> INT = Codec.STRING.xmap(Integer::parseInt, (integer) -> Integer.toString(integer));
}
