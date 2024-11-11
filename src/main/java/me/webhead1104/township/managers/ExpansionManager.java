package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@NoArgsConstructor
public class ExpansionManager {

    public void openExpansionMenu(Player player, Expansion expansion) {
        Inventory inventory = Township.getWorldManager().getWorld(player);
        player.getInventory().clear();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        ItemStack itemStack = new ItemBuilder(Material.LIME_CONCRETE, "expansion")
                .pdcSetString(ItemBuilder.expansionDataKey, expansion.toString())
                .material(Material.LIME_CONCRETE)
                .displayName(MM."Expansion")
                .build();
        int expansionSlot = expansion.getSlot();
        inventory.setItem(expansionSlot, itemStack);
        inventory.setItem(expansionSlot + 1, itemStack);
        inventory.setItem(expansionSlot + 2, itemStack);
        inventory.setItem(expansionSlot + 9, itemStack);
        inventory.setItem(expansionSlot + 10, itemStack);
        inventory.setItem(expansionSlot + 11, itemStack);
        inventory.setItem(expansionSlot + 18, itemStack);
        inventory.setItem(expansionSlot + 19, itemStack);
        inventory.setItem(expansionSlot + 20, itemStack);
        player.openInventory(inventory);
        ItemBuilder expansionPrice = new ItemBuilder(MenuItems.expansionPrice);
        expansionPrice.displayName(user.getCoins() >= expansion.getPrice() ?
                MM."<gold>Coins needed: <white>\{user.getCoins()}/\{expansion.getPrice()}" :
                MM."<gold>Coins needed: <red>\{user.getCoins()}/\{expansion.getPrice()}");
        expansionPrice.material(Material.GOLD_BLOCK);
        player.getInventory().setItem(2, expansionPrice.build());
        ItemBuilder expansionBuy = new ItemBuilder(MenuItems.expansionBuy);
        if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getPrice()) {
            expansionBuy.displayName(MM."Click to buy!");
            expansionBuy.lore(List.of(MM."<gold>Coins needed: <green>\{user.getCoins()}/\{expansion.getPrice()}", MM."<red>Population needed: <green>\{user.getPopulation()}/\{expansion.getPopulationNeeded()}"));
            expansionBuy.material(Material.LIME_CONCRETE);
        } else {
            expansionBuy.displayName(MM.":(");
            expansionBuy.lore(List.of(MM."<gold>Coins needed: <red>\{user.getCoins()}/\{expansion.getPrice()}", MM."<red>Population needed: <red>\{user.getPopulation()}/\{expansion.getPopulationNeeded()}"));
            expansionBuy.material(Material.RED_CONCRETE);
        }
        expansionBuy.pdcSetString(ItemBuilder.expansionDataKey, expansion.toString());
        player.getInventory().setItem(4, expansionBuy.build());
        ItemBuilder expansionPopulation = new ItemBuilder(MenuItems.expansionPopulation);
        expansionPopulation.displayName(user.getPopulation() >= expansion.getPopulationNeeded() ?
                MM."<red>Population needed: <white>\{user.getPopulation()}/\{expansion.getPopulationNeeded()}" :
                MM."<red>Population needed: \{user.getPopulation()}/\{expansion.getPopulationNeeded()}");
        expansionPopulation.material(Material.BLUE_CONCRETE);
        player.getInventory().setItem(6, expansionPopulation.build());
        player.getInventory().setItem(0, MenuItems.backButton);
    }

    public void buyExpansion(Player player, Expansion expansion) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getCoins() >= expansion.getPrice() && user.getPopulation() >= expansion.getPopulationNeeded()) {
            user.setCoins(user.getCoins() - expansion.getPrice());
            removeExpansion(player, expansion);
            Township.getWorldManager().openWorldMenu(player);
        }
    }

    public void removeExpansion(Player player, Expansion expansion) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        for (Integer i : Utils.thing("3x3", expansion.getSlot())) {
            user.getWorld().getSection(expansion.getSection()).setSlot(i, WorldTileType.GRASS);
        }
    }
}
