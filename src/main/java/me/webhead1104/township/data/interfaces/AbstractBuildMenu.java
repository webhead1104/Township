package me.webhead1104.township.data.interfaces;

import me.webhead1104.township.data.enums.BuildingType;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractBuildMenu {
    public abstract List<BuildingType> open(Player player, int page);
}
