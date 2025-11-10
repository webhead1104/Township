package me.webhead1104.towncraft.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@NoArgsConstructor
@ConfigSerializable
public class ConstructionMaterials {
    private int glass;
    private int brick;
    private int slab;
}
