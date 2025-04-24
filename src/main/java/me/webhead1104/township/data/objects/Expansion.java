package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;

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
        return Township.GSON.fromJson(json, Expansion.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}
