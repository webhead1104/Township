package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@AllArgsConstructor
@ConfigSerializable
public class ConstructionMaterials {
    private int glass;
    private int brick;
    private int slab;

    public ConstructionMaterials(String string) {
        String[] split = string.split(" ");
        this.glass = Integer.parseInt(split[0]);
        this.brick = Integer.parseInt(split[1]);
        this.slab = Integer.parseInt(split[2]);
    }
}
