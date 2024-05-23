package me.webhead1104.township;

import me.flame.menus.menu.Menu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.EnumSet;

public class LevelMenu {

    public Menu levelMenu = Menu.create(ChatColor.AQUA + "Level Menu", 5, EnumSet.allOf(Modifier.class));
    public LevelMenu() {}
    //todo levels
    public void levelMenu(Player player) {
        int level = Database.getData(player.getUniqueId()).get("level").getAsInt();
        levelMenu.getFiller().fill(MenuItems.glass);
        levelMenu.setItem(22, MenuItems.levelMenuMiddleItem.editor().setName(ChatColor.AQUA + "Level "+level).done());
        levelMenu.setItem(36, MenuItems.backButton);
        levelMenu.open(player);
    }
}