package me.webhead1104.township.tiles;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.BuildingType;

@Getter
@Setter
public abstract class BuildingTile extends Tile {
    private BuildingType buildingType;
    private int buildingSlot;

    protected BuildingTile(BuildingType buildingType, int buildingSlot) {
        this.buildingType = buildingType;
        this.buildingSlot = buildingSlot;
    }

    // Secondary constructor to allow deferring buildingType assignment to avoid circular init
    protected BuildingTile(int buildingSlot) {
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
