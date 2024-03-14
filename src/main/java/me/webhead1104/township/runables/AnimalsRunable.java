package me.webhead1104.township.runables;

import me.flame.menus.menu.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.webhead1104.township.Township;
import java.util.Objects;

public class AnimalsRunable extends BukkitRunnable {

    private final Township plugin;
    private int timeLeft;
    private Player player;
    private String whatone;
    private PaginatedMenu menu;

    public AnimalsRunable(Township plugin, int timeLeft, Player player, String whatone) {
        this.plugin = plugin;
        this.timeLeft = timeLeft;
        this.whatone = whatone;
        this.player = player;
        if (whatone.startsWith("cow")) {
            this.menu = plugin.getCommand().cowshed;
        } else if (whatone.startsWith("chicken")) {
            this.menu = plugin.getCommand().chickencoop;
        } else if (whatone.startsWith("sheep")) {
            this.menu = plugin.getCommand().sheepfarm;
        }
    }


    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 0) {
            cancel();
            player.sendMessage("done");
            if (whatone.startsWith("cow")) {
                int one = 0, two = 0, three = 0, four = 0, five = 0;
                if (Objects.equals(whatone, "cow 1")) {
                    one = 1;
                    menu.getItem(11).editor().setLore(ChatColor.GOLD + "Click to pickup!").done();
                }
                if (Objects.equals(whatone, "cow 2")) {
                    two = 1;
                    menu.getItem(12).editor().setLore(ChatColor.GOLD + "Click to pickup!").done();
                }
                if (Objects.equals(whatone, "cow 3")) {
                    three = 1;
                    menu.getItem(13).editor().setLore(ChatColor.GOLD + "Click to pickup!").done();
                }
                if (Objects.equals(whatone, "cow 4")) {
                    four = 1;
                    menu.getItem(14).editor().setLore(ChatColor.GOLD + "Click to pickup!").done();
                }
                if (Objects.equals(whatone, "cow 5")) {
                    five = 1;
                    menu.getItem(15).editor().setLore(ChatColor.GOLD + "Click to pickup!").done();
                }
                String value = "one " + one + " two " + two + " three " + three + " four " + four + " five " + five + " end";
                plugin.setPlayerData(player, "cowshedmilk", value);
            }

            player.sendMessage("hello you have " + timeLeft + " left");
            if (Objects.equals(whatone, "cow 1"))
                menu.getItem(11).editor().setLore("" + ChatColor.GOLD + timeLeft).done();
            if (Objects.equals(whatone, "cow 2"))
                menu.getItem(12).editor().setLore("" + ChatColor.GOLD + timeLeft).done();
            if (Objects.equals(whatone, "cow 3"))
                menu.getItem(13).editor().setLore("" + ChatColor.GOLD + timeLeft).done();
            if (Objects.equals(whatone, "cow 4"))
                menu.getItem(14).editor().setLore("" + ChatColor.GOLD + timeLeft).done();
            if (Objects.equals(whatone, "cow 5"))
                menu.getItem(15).editor().setLore("" + ChatColor.GOLD + timeLeft).done();

        }
    }
}
