package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlotType {
    NONE(ItemType.NONE, -1, -1, "none_plot"),
    WHEAT(ItemType.WHEAT, 0, 1, "wheat_plot"),
    CORN(ItemType.CORN, 1, 3, "corn_plot"),
    CARROT(ItemType.CARROT, 2, 4, "carrot_plot"),
    SUGARCANE(ItemType.SUGARCANE, 3, 7, "sugarcane_plot");
    private final ItemType itemType;
    private final int price;
    private final int levelNeeded;
    private final String id;
}
