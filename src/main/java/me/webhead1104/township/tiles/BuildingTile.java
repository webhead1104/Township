package me.webhead1104.township.tiles;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;

@Getter
@Setter
public abstract class BuildingTile extends Tile {
    private Key buildingType;
    private int buildingSlot;

    protected BuildingTile(Key buildingType, int buildingSlot) {
        this.buildingType = buildingType;
        this.buildingSlot = buildingSlot;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BuildingTile buildingTile) {
            return buildingTile.getBuildingType() == buildingType && buildingTile.getBuildingSlot() == buildingSlot;
        }
        return super.equals(object);
    }
}
