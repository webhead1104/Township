package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class BarnUpgrade {
    private int id;
    private int toolsNeeded;
    private int barnStorage;
}
