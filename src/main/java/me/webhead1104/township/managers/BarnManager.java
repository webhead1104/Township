package me.webhead1104.township.managers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.data.objects.BarnUpgrade;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
@Getter
public class BarnManager {
    private final Map<Integer, BarnUpgrade> upgradeMap = new HashMap<>();

    public void loadUpgrades() {
        AtomicInteger storage = new AtomicInteger(70);
        AtomicInteger a = new AtomicInteger(20);
        for (int i = 1; i < 101; i++) {
            if (i == 1) {
                upgradeMap.put(i, new BarnUpgrade(i, i, 70));
                continue;
            }
            if (i == 3) {
                a.set(25);
            } else if (i == 39) {
                a.set(50);
            } else if (i == 59) {
                a.set(75);
            }
            int b = storage.get() + a.get();
            storage.set(b);
            upgradeMap.put(i, new BarnUpgrade(i, i, storage.get() + a.get()));
        }
    }
}
