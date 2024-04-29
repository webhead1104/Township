package me.webhead1104.township;

import me.flame.menus.menu.Menu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.utils.Items;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.EnumSet;

public class LevelMenu {

    Township plugin;
    public Menu levelMenu = Menu.create(ChatColor.AQUA + "Level Menu", 5, EnumSet.allOf(Modifier.class));
    public LevelMenu(Township plugin) {
        this.plugin = plugin;
    }
    //todo levels
    public void levelMenu(Player player) {
        int level = Integer.parseInt(Database.getPlayerData(player,"level"));
        levelMenu.getFiller().fill(Items.glass);
        levelMenu.setItem(22, Items.levelMenuMiddleItem.editor().setName(ChatColor.AQUA + "Level "+level).done());
        levelMenu.setItem(36, Items.backButton);
        levelMenu.open(player);
    }
}