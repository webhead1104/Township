package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.Township;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class Expansion {
    private int section;
    private int slot;
    private int price;
    private int populationNeeded;

    public static Expansion fromJson(String json) {
        return Township.GSON.fromJson(json, Expansion.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}
