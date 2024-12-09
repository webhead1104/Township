package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.objects.Animals;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


@NoArgsConstructor
public class AnimalsManager {

    public void openAnimalMenu(Player player, AnimalType type) {
        // animals 11-17 feed 36
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (!(user.getLevel().getLevel() >= type.getLevelNeeded())) return;
        Animals animals = user.getAnimals();
        Inventory inventory = Bukkit.createInventory(null, 54, type.getMenuTitle());
        int slot = 11;
        for (int i = 0; i < 6; ++i) {
            if (animals.getProduct(type, i)) {
                ItemBuilder builder = new ItemBuilder(type.getProductType().getItemStack());
                builder.pdcSetString(Keys.animalTypeKey, type.name());
                builder.pdcSetInt(Keys.slot, i);
                inventory.setItem(slot + 9, builder.build());
            }
            if (animals.getFeed(type, i)) {
                ItemBuilder builder = new ItemBuilder(type.getAnimalItemStack());
                builder.lore(Msg.format("<gold>Time: 0"));
                inventory.setItem(slot, builder.build());
            } else inventory.setItem(slot, type.getAnimalItemStack());
            slot++;
        }
        ItemBuilder builder = new ItemBuilder(type.getFeedType().getItemStack());
        builder.lore(Msg.format("<white>" + user.getBarn().getItem(type.getFeedType())), 0);
        builder.pdcSetString(Keys.animalTypeKey, type.name());
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
                }
            }
        }
        openAnimalMenu(player, type);
    }

    public void pickup(Player player, AnimalType type, int slot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        if (animals.getProduct(type, slot)) {
            animals.setProduct(type, slot, false);
            animals.setFeed(type, slot, false);
            user.getBarn().addAmountToItem(type.getProductType(), 1);
            user.getLevel().addXp(type.getXpGivenOnClaim());
            openAnimalMenu(player, type);
        }
    }
}
