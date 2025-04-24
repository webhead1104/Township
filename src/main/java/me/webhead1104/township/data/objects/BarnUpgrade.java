package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BarnUpgrade {
    private int id;
    private int toolsNeeded;
    private int barnStorage;

    public BarnUpgrade(int id, int toolsNeeded, int barnStorage) {
        this.id = id;
        this.toolsNeeded = toolsNeeded;
        this.barnStorage = barnStorage;
    }
}
