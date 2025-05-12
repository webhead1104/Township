package me.webhead1104.township.utils;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface MenuUtils {
    static List<Integer> borderSlots(Inventory inventory) {
        int size = inventory.getSize();
        List<Integer> slots = new ArrayList<>();
        int lastRowStart = size - 9; // Precompute last row start index

        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= lastRowStart || i % 9 == 0 || (i + 1) % 9 == 0) {
                slots.add(i);
            }
        }
        return slots;
    }

    static List<Integer> nonBorderSlots(Inventory inventory) {
        Set<Integer> excludedSet = new HashSet<>(borderSlots(inventory));

        List<Integer> remainingSlots = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (!excludedSet.contains(i)) {
                remainingSlots.add(i);
            }
        }

        return remainingSlots;
    }
}
