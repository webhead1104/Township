package me.webhead1104.township.data.buildMenus;

import me.webhead1104.township.data.enums.BuildingType;
import me.webhead1104.township.data.interfaces.AbstractBuildMenu;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmingBuildMenu extends AbstractBuildMenu {
    private static final Map<Integer, List<BuildingType>> BUILDINGS = new HashMap<>();

    static {
        List<BuildingType> buildingTypes = new ArrayList<>(List.of(BuildingType.COWSHED, BuildingType.CHICKEN_COOP));
        int page = 0;
        int i = 0;
        BUILDINGS.put(page, new ArrayList<>(6));
        for (BuildingType value : buildingTypes) {
            BUILDINGS.get(page).add(value);
            if (i == 6) {
                page++;
                i = 0;
                BUILDINGS.put(page, new ArrayList<>(6));
            }
            i++;
        }
    }

    @Override
    public List<BuildingType> open(Player player, int page) {
        return new ArrayList<>(BUILDINGS.get(page));
    }
}
