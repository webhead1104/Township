package me.webhead1104.township.data.objects;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildingType;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ConfigSerializable
public class PurchasedBuildings {
    private final Map<BuildingType, Map<Integer, Boolean>> purchasedBuildings = new HashMap<>();

    public PurchasedBuildings() {
        for (BuildingType value : BuildingType.values()) {
            purchasedBuildings.put(value, new HashMap<>());
            int i = 0;
            for (Building building : value.getBuildings().values()) {
                purchasedBuildings.get(value).put(i++, false);
            }
        }
    }

    public boolean isPurchased(BuildingType buildingType, int slot) {
        return purchasedBuildings.get(buildingType).get(slot);
    }

    public int amountPurchased(BuildingType buildingType) {
        int i = 0;
        for (Boolean value : purchasedBuildings.get(buildingType).values()) {
            if (value) i++;
        }
        return i;
    }

    public void setPurchased(BuildingType buildingType, int slot, boolean purchased) {
        purchasedBuildings.get(buildingType).replace(slot, purchased);
    }

    public void recalculatePopulation(Player player) {
        AtomicInteger population = new AtomicInteger();
        AtomicInteger maxPopulation = new AtomicInteger();
        purchasedBuildings.forEach((buildingType, map) -> map.forEach((key, value) -> {
            if (value) {
                Building building = buildingType.getBuildings().get(key);
                population.addAndGet(building.getPopulationIncrease());
                maxPopulation.addAndGet(building.getMaxPopulationIncrease());
            }
        }));
        Township.getUserManager().getUser(player.getUniqueId()).setPopulation(population.get());
        Township.getUserManager().getUser(player.getUniqueId()).setMaxPopulation(maxPopulation.get());
    }
}
