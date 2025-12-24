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
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Keep
public class DurationSerializer extends TowncraftSerializer<Duration> {

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

    @Override
    public Class<Duration> getType() {
        return Duration.class;
    }
}