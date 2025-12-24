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
package me.webhead1104.towncraft.utils;

import lombok.experimental.UtilityClass;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@UtilityClass
public class Utils {
    private static final Random RANDOM = new Random();

    public static String thing2(String string) {
        return StringUtils.capitalize(string.replaceAll("_", " ").toLowerCase());
    }

    public static String format(Instant instant1, Instant instant2) {
        Duration duration = Duration.between(instant1, instant2);
        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();

        StringBuilder formatted = new StringBuilder();

        if (days > 0) {
            formatted.append(days).append("d ");
        }
        if (hours > 0) {
            formatted.append(hours).append("h ");
        }
        if (minutes > 0) {
            formatted.append(minutes).append("m ");
        }
        if (seconds > 0) {
            formatted.append(seconds).append("s");
        }

        return formatted.toString().trim(); // Remove trailing space
    }


    public static TowncraftItemStack getItemStack(String name, TowncraftMaterial material) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(material);
        itemStack.setName(Msg.format("<white>%s", name));
        return itemStack;
    }

    public Component addResourceLine(String resourceName, int current, int required, Object... args) {
        resourceName = resourceName.formatted(args);
        String color = current >= required ? "<green>" : "<red>";
        return Msg.format("%s<white>: %s%d/%d", resourceName, color, current, required);
    }

    public ItemType.Item selectWeightedItem(
            List<ItemType.Item> availableItems,
            List<ItemType.Item> alreadySelected,
            int playerLevel) {

        List<ItemType.Item> pool = availableItems.stream()
                .filter(item -> !alreadySelected.contains(item))
                .toList();

        if (pool.isEmpty()) return null;

        Map<ItemType.Item, Integer> adjustedWeights = new HashMap<>();
        int totalWeight = 0;

        for (ItemType.Item item : pool) {
            int adjustedWeight = calculateAdjustedWeight(
                    item.getBaseWeight(),
                    item.getLevelNeeded(),
                    playerLevel
            );
            adjustedWeights.put(item, adjustedWeight);
            totalWeight += adjustedWeight;
        }

        if (totalWeight <= 0) return pool.getFirst();

        int randomValue = RANDOM.nextInt(totalWeight);
        int cumulative = 0;

        for (ItemType.Item item : pool) {
            cumulative += adjustedWeights.get(item);
            if (randomValue < cumulative) {
                return item;
            }
        }

        return pool.getFirst();
    }

    public int calculateAdjustedWeight(int baseWeight, int itemUnlockLevel, int playerLevel) {
        int levelDiff = playerLevel - itemUnlockLevel;

        if (levelDiff >= 0 && levelDiff <= 5) {
            return (int) (baseWeight * 1.5);  // Recently unlocked: 50% MORE common
        } else if (levelDiff > 5 && levelDiff <= 10) {
            return baseWeight;  // Normal
        } else if (levelDiff > 10 && levelDiff <= 20) {
            return (int) (baseWeight * 0.75);  // 25% less common
        } else if (levelDiff > 20) {
            return (int) (baseWeight * 0.5);  // 50% less common
        } else {
            return 0;  // Not unlocked yet
        }
    }
}
