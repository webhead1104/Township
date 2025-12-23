package me.webhead1104.towncraft.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.features.world.build.BuildingType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.*;

@Getter
@ConfigSerializable
public class PurchasedBuildings {
    private final Map<Key, List<PurchasedBuilding>> purchasedBuildings = new HashMap<>();

    public PurchasedBuildings() {
        purchasedBuildings.put(Towncraft.key("barn"), new ArrayList<>(List.of(new PurchasedBuilding(0, 27, true, Towncraft.key("barn")))));
        purchasedBuildings.put(Towncraft.key("plot"), new ArrayList<>(List.of(new PurchasedBuilding(0, 27, true, Towncraft.key("plot")))));
        purchasedBuildings.put(Towncraft.key("train"), new ArrayList<>(List.of(new PurchasedBuilding(0, 28, true, Towncraft.key("train")))));
    }

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

    public @Nullable BuildingType.Building getNextBuilding(Key buildingType) {
        List<BuildingType.Building> buildings = Towncraft.getDataLoader(BuildingType.class).get(buildingType);
        if (!purchasedBuildings.containsKey(buildingType)) {
            return buildings.getFirst();
        }

        for (PurchasedBuilding purchasedBuilding : purchasedBuildings.get(buildingType)) {
            if (!purchasedBuilding.isPlaced()) {
                return buildings.get(purchasedBuilding.getSlot()).withNeedToBePlaced(true);
            }
        }

        if (buildings.size() > purchasedBuildings.get(buildingType).size()) {
            return buildings.get(amountPurchased(buildingType));
        }
        return null;
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
            return Towncraft.getDataLoader(BuildingType.class).get(buildingType).get(slot);
        }
    }
}
