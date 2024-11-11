package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.objects.Animals;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@NoArgsConstructor
public class AnimalsManager {

    public void openAnimalMenu(Player player, AnimalType type) {
        // animals 11-17 feed 36
        Inventory inventory = Bukkit.createInventory(null, 54, type.getMenuTitle());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        int slot = 11;
        for (int i = 0; i < 6; ++i) {
            if (animals.getProduct(type, i)) inventory.setItem(slot + 9, type.getProductItemStack());
            if (animals.getFeed(type, i)) {
                ItemBuilder builder = new ItemBuilder(type.getAnimalItemStack());
                builder.lore(MM."<gold>Time: 0");
                inventory.setItem(slot, builder.build());
            } else inventory.setItem(slot, type.getAnimalItemStack());
            slot++;
        }
        ItemBuilder builder = new ItemBuilder(type.getFeedItemStack());
        builder.lore(MM."<white>\{user.getBarn().getItem(type.getFeedType())}", 0);
        inventory.setItem(36, builder.build());
        inventory.setItem(53, MenuItems.backButton);
        player.openInventory(inventory);
    }

    public void feed(Player player, AnimalType type) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        int feed = user.getBarn().getItem(type.getFeedType());
        if (feed >= 1) {
            for (int i = 0; i < 6; ++i) {
                if (user.getBarn().getItem(type.getFeedType()) >= 1) {
                    if (animals.getFeed(type, i)) {
                        continue;
                    }
                    user.getBarn().removeAmountFromItem(type.getFeedType(), 1);
                    animals.setFeed(type, i, true);
                    animals.setProduct(type, i, true);
                    openAnimalMenu(player, type);
                }
            }
        }
    }

    public void pickup(Player player, AnimalType type, int slot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        int product = switch (slot) {
            case 21 -> 1;
            case 22 -> 2;
            case 23 -> 3;
            case 24 -> 4;
            case 25 -> 5;
            case 26 -> 6;
            default -> 0;
        };
        if (animals.getProduct(type, product)) {
            animals.setProduct(type, product, false);
            animals.setFeed(type, product, false);
            openAnimalMenu(player, type);
        }
    }
}
