package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expansion {
    private int section;
    private int slot;
    private int price;
    private int populationNeeded;

    public Expansion(int section, int slot, int price, int populationNeeded) {
        this.section = section;
        this.slot = slot;
        this.price = price;
        this.populationNeeded = populationNeeded;
    }

    public static Expansion fromJson(String json) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
