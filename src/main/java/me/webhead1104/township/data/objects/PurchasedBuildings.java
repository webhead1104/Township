package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.BuildingType;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ConfigSerializable
public class PurchasedBuildings {
    private final Map<Key, List<PurchasedBuilding>> purchasedBuildings = new HashMap<>();

    public boolean isPurchased(Key buildingType, int slot) {
        if (!purchasedBuildings.containsKey(buildingType)) return false;
        return purchasedBuildings.get(buildingType).stream().mapToInt(PurchasedBuilding::getSlot).anyMatch(a -> Objects.equals(a, slot));
    }

    public int amountPurchased(Key buildingType) {
        if (!purchasedBuildings.containsKey(buildingType)) return 0;
        return purchasedBuildings.get(buildingType).size();
    }

    public int amountPlaced(Key buildingType) {
        if (!purchasedBuildings.containsKey(buildingType)) return 0;
        return purchasedBuildings.get(buildingType).stream().filter(PurchasedBuilding::isPlaced).toList().size();
    }

    public Wrapper getNextBuilding(Key buildingType) {
        if (!purchasedBuildings.containsKey(buildingType)) {
            return new Wrapper(0, false);
        }

        for (PurchasedBuilding purchasedBuilding : purchasedBuildings.get(buildingType)) {
            if (!purchasedBuilding.isPlaced()) {
                return new Wrapper(purchasedBuilding.getSlot(), true);
            }
        }

        if (BuildingType.get(buildingType).size() > purchasedBuildings.get(buildingType).size()) {
            return new Wrapper(-2, false);
        }
        return new Wrapper(-1, false);
    }

    public void purchase(Key buildingType, int slot) {
        if (!purchasedBuildings.containsKey(buildingType)) {
            purchasedBuildings.put(buildingType, new ArrayList<>());
        }
        purchasedBuildings.get(buildingType).add(new PurchasedBuilding(slot, -1, false, buildingType));
    }

    public Optional<PurchasedBuilding> getPurchasedBuilding(Key buildingType, int slot) {
        if (!purchasedBuildings.containsKey(buildingType)) return Optional.empty();
        return Optional.ofNullable(purchasedBuildings.get(buildingType).get(slot));
    }

    public void recalculatePopulation(Player player) {
        AtomicInteger population = new AtomicInteger();
        AtomicInteger maxPopulation = new AtomicInteger();
        purchasedBuildings.forEach((buildingType, list) -> list.forEach(value -> {
            BuildingType.Building building = value.getBuilding();
            population.addAndGet(building.getPopulationIncrease());
            maxPopulation.addAndGet(building.getMaxPopulationIncrease());
        }));
        Township.getUserManager().getUser(player.getUniqueId()).setPopulation(population.get());
        Township.getUserManager().getUser(player.getUniqueId()).setMaxPopulation(maxPopulation.get());
    }

    public record Wrapper(int slot, boolean placed) {
    }

    @Getter
    @Setter
    @ConfigSerializable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PurchasedBuilding {
        private int slot;
        private int section;
        private boolean placed;
        private Key buildingType;

        public BuildingType.Building getBuilding() {
            return BuildingType.get(buildingType).get(slot);
        }
    }
}
