package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.menus.ExpansionMenu;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class ExpansionManager {

    public void openExpansionMenu(Player player, Expansion expansion) {
        Township.getViewFrame().open(ExpansionMenu.class, player, expansion);
    }
}
