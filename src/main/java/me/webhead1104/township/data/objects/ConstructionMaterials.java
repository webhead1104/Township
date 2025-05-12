package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstructionMaterials {
    private int glass;
    private int brick;
    private int slab;

    public ConstructionMaterials(int glass, int brick, int slab) {
        this.glass = glass;
        this.brick = brick;
        this.slab = slab;
    }
}
