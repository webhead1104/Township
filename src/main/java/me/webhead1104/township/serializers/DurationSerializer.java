package me.webhead1104.township.serializers;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationSerializer implements TypeSerializer<Duration> {

    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "(?:(\\d+)\\s*d)?\\s*" +
                    "(?:(\\d+)\\s*h)?\\s*" +
                    "(?:(\\d+)\\s*(?:m|min))?\\s*" +
                    "(?:(\\d+)\\s*s)?",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public Duration deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        String duration = node.getString();
        if (duration == null) throw new SerializationException("Cannot deserialize a null duration!");
        Matcher matcher = DURATION_PATTERN.matcher(duration.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration: " + duration);
        }

        long days = matcher.group(1) != null ? Long.parseLong(matcher.group(1)) : 0;
        long hours = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : 0;
        long minutes = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;
        long seconds = matcher.group(4) != null ? Long.parseLong(matcher.group(4)) : 0;

        return Duration.ofDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Duration obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Cannot serialize a null instant!");
        throw new UnsupportedOperationException("Not Implemented");
    }
}