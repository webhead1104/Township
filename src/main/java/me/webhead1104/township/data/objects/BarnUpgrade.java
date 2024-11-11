package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BarnUpgrade {
    private int id;
    private int toolsNeeded;
    private int barnStorage;
}
