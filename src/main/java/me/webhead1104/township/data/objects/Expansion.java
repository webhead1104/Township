package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@ConfigSerializable
public class Expansion {
    public static final int LATEST_VERSION = 1;
    private int version = LATEST_VERSION;
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
        return new Expansion(section, slot, 100, 20);
    }
}
