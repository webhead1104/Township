package me.webhead1104.township;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.webhead1104.township.runables.AnimalsRunable;
import java.util.Objects;

public class Animals {
 Township plugin;

 public Animals(Township plugin) {
  this.plugin = plugin;
 }


 public void cowshed(InventoryClickEvent event) {
  Player player = (Player) event.getWhoClicked();
  String input = plugin.getDatabase().getPlayerData(player, "cowshedmilk");
  int feed = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "cowfeed"));
  if (Integer.parseInt(input.substring(input.indexOf("one") + 4, input.indexOf("two") - 1)) == 1) plugin.getCommand().cowshed.setItem(20, plugin.getItems().cowshedMilk.editor().setCustomModelData(1).done());
  if (Integer.parseInt(input.substring(input.indexOf("two") + 4, input.indexOf("three") - 1)) == 1) plugin.getCommand().cowshed.setItem(21, plugin.getItems().cowshedMilk.editor().setCustomModelData(2).done());
  if (Integer.parseInt(input.substring(input.indexOf("three") + 6, input.indexOf("four") - 1)) == 1) plugin.getCommand().cowshed.setItem(22, plugin.getItems().cowshedMilk.editor().setCustomModelData(3).done());
  if (Integer.parseInt(input.substring(input.indexOf("four") + 5, input.indexOf("five") - 1)) == 1) plugin.getCommand().cowshed.setItem(23, plugin.getItems().cowshedMilk.editor().setCustomModelData(4).done());
  if (Integer.parseInt(input.substring(input.indexOf("five") + 5, input.indexOf("end") - 1)) == 1) plugin.getCommand().cowshed.setItem(24, plugin.getItems().cowshedMilk.editor().setCustomModelData(5).done());
  plugin.getCommand().cowshed.setItem(34, plugin.getItems().backButton);
  plugin.getCommand().cowshed.setItem(11, plugin.getItems().cow);
  plugin.getCommand().cowshed.setItem(12, plugin.getItems().cow);
  plugin.getCommand().cowshed.setItem(13, plugin.getItems().cow);
  plugin.getCommand().cowshed.setItem(14, plugin.getItems().cow);
  plugin.getCommand().cowshed.setItem(15, plugin.getItems().cow);
  plugin.getCommand().cowshed.setItem(36, plugin.getItems().cowshedFeed.editor().setLore(ChatColor.GOLD + "" + feed).done());
  plugin.getCommand().cowshed.open(player);
 }

 public void cowshedFeed(InventoryClickEvent event) {
  Player player = (Player) event.getWhoClicked();
  String input = plugin.getDatabase().getPlayerData(player, "cowshedfeed");
  int feed = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "cowfeed"));
  int cow1feed = Integer.parseInt(input.substring(input.indexOf("one") + 4, input.indexOf("two") - 1));
  int cow2feed = Integer.parseInt(input.substring(input.indexOf("two") + 4, input.indexOf("three") - 1));
  int cow3feed = Integer.parseInt(input.substring(input.indexOf("three") + 6, input.indexOf("four") - 1));
  int cow4feed = Integer.parseInt(input.substring(input.indexOf("four") + 5, input.indexOf("five") - 1));
  int cow5feed = Integer.parseInt(input.substring(input.indexOf("five") + 5, input.indexOf("end") - 1));
  if (feed >= 1) {
   if (cow1feed == 0) {
    cow1feed = 1;
    feed--;
    new AnimalsRunable(plugin, 60 * 5, player, "cow 1").runTaskTimer(plugin, 0, 20);
   } else if (cow2feed == 0) {
    cow2feed = 1;
    feed--;
    new AnimalsRunable(plugin, 60 * 5, player, "cow 2").runTaskTimer(plugin, 0, 20);
   } else if (cow3feed == 0) {
    cow3feed = 1;
    feed--;
    new AnimalsRunable(plugin, 60 * 5, player, "cow 3").runTaskTimer(plugin, 0, 20);
   } else if (cow4feed == 0) {
    cow4feed = 1;
    feed--;
    new AnimalsRunable(plugin, 60 * 5, player, "cow 4").runTaskTimer(plugin, 0, 20);
   } else if (cow5feed == 0) {
    cow5feed = 1;
    feed--;
    new AnimalsRunable(plugin, 60 * 5, player, "cow 5").runTaskTimer(plugin, 0, 20);
   }
   plugin.getCommand().cowshed.getItem(36).editor().setLore("" + ChatColor.GOLD + feed).done();


   String output = "one " + cow1feed + " two " + cow2feed + " three " + cow3feed + " four " + cow4feed + " five " + cow5feed + " end";
   plugin.getDatabase().setPlayerData(player, "cowfeed", String.valueOf(feed));
   plugin.database.setPlayerData(player, "cowshedfeed", output);
  }
 }

 public void milk(InventoryClickEvent event, Player player) {
  plugin.getDatabase().setPlayerData(player, "milk", String.valueOf(Integer.parseInt(plugin.getDatabase().getPlayerData(player, "milk")) + 1));
  plugin.getCommand().cowshed.removePageItem(event.getSlot());
  String input = plugin.getDatabase().getPlayerData(player, "cowshedmilk");
  int cow1milk = Integer.parseInt(input.substring(input.indexOf("one") + 4, input.indexOf("two") - 1));
  int cow2milk = Integer.parseInt(input.substring(input.indexOf("two") + 4, input.indexOf("three") - 1));
  int cow3milk = Integer.parseInt(input.substring(input.indexOf("three") + 6, input.indexOf("four") - 1));
  int cow4milk = Integer.parseInt(input.substring(input.indexOf("four") + 5, input.indexOf("five") - 1));
  int cow5milk = Integer.parseInt(input.substring(input.indexOf("five") + 5, input.indexOf("end") - 1));
  if (Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 1) cow1milk = 0;
  if (Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 2) cow2milk = 0;
  if (Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 3) cow3milk = 0;
  if (Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 4) cow4milk = 0;
  if (Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 5) cow5milk = 0;
  String value = "one " + cow1milk + " two " + cow2milk + " three " + cow3milk + " four " + cow4milk + " five " + cow5milk + " end";
  plugin.getDatabase().setPlayerData(player, "cowshedmilk", value);
  plugin.getCommand().cowshed.getItem(event.getSlot() - 9).editor().setLore(ChatColor.GOLD + "Click the feed to feed me!").done();
 }
}