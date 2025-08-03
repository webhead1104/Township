package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@ConfigSerializable
@NoArgsConstructor
public class Expansion {
    private int section;
    private int slot;
    private int price;
    private int populationNeeded;

    public Expansion(int populationNeeded, int price, int slot, int section) {
        this.populationNeeded = populationNeeded;
        this.price = price;
        this.slot = slot;
        this.section = section;
    }

    public static Expansion defaultExpansion(int section, int slot) {
        return new Expansion(20, 100, slot, section);
    }
}
