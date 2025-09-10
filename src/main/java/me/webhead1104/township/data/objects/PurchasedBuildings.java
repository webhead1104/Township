package me.webhead1104.township.data.objects;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildingType;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ConfigSerializable
public class PurchasedBuildings {
    private final Map<BuildingType, List<Integer>> purchasedBuildings = new HashMap<>();

    public boolean isPurchased(BuildingType buildingType, int slot) {
        if (!purchasedBuildings.containsKey(buildingType)) return false;
        return purchasedBuildings.get(buildingType).contains(slot);
    }

    public int amountPurchased(BuildingType buildingType) {
        if (!purchasedBuildings.containsKey(buildingType)) return 0;
        return purchasedBuildings.get(buildingType).size();
    }

    public void purchase(BuildingType buildingType, int slot) {
        if (!purchasedBuildings.containsKey(buildingType)) {
            purchasedBuildings.put(buildingType, new ArrayList<>());
        }
        purchasedBuildings.get(buildingType).add(slot);
    }

    public void recalculatePopulation(Player player) {
        AtomicInteger population = new AtomicInteger();
        AtomicInteger maxPopulation = new AtomicInteger();
        purchasedBuildings.forEach((buildingType, list) -> list.forEach(value -> {
            Building building = buildingType.getBuildings().get(value);
            population.addAndGet(building.getPopulationIncrease());
            maxPopulation.addAndGet(building.getMaxPopulationIncrease());
        }));
        Township.getUserManager().getUser(player.getUniqueId()).setPopulation(population.get());
        Township.getUserManager().getUser(player.getUniqueId()).setMaxPopulation(maxPopulation.get());
    }
}
