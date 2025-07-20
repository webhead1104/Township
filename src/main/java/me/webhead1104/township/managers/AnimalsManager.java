package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.menus.AnimalMenu;
import org.bukkit.entity.Player;


@NoArgsConstructor
public class AnimalsManager {

    public void openAnimalMenu(Player player, AnimalType type) {
        Township.getViewFrame().open(AnimalMenu.class, player, type);
    }
}
