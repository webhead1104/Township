package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.objects.Animals;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
public class AnimalsManager {

    public void openAnimalMenu(Player player, AnimalType type) {
        // animals 11-17 feed 36
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        if (animals.isUnlocked(type)) {
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
            builder.lore(Msg.format("<white>" + user.getBarn().getItem(type.getFeedType())));
            builder.pdcSetString(Keys.animalTypeKey, type.name());
            inventory.setItem(36, builder.build());
            inventory.setItem(53, MenuItems.backButton);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    int slot = 11;
                    Animals animals = Township.getUserManager().getUser(player.getUniqueId()).getAnimals();
                    for (int i = 0; i < 6; ++i) {
                        if (animals.getInstant(type, i).equals(Instant.EPOCH)) {
                            slot++;
                            continue;
                        }
                        if (Instant.now().isAfter(animals.getInstant(type, i).minusSeconds(1))) {
                            animals.setFeed(type, i, false);
                            animals.setProduct(type, i, true);
                            animals.setInstant(type, i, Instant.EPOCH);
                            ItemBuilder builder = new ItemBuilder(type.getProductType().getItemStack());
                            builder.pdcSetString(Keys.animalTypeKey, type.name());
                            builder.pdcSetInt(Keys.slot, i);
                            inventory.setItem(slot + 9, builder.build());
                            Objects.requireNonNull(inventory.getItem(slot)).editMeta(ItemMeta.class, meta -> meta.lore(List.of()));
                            slot++;
                            continue;
                        }
                        String string = Utils.format(Instant.now(), animals.getInstant(type, i));
                        Objects.requireNonNull(inventory.getItem(slot)).editMeta(ItemMeta.class, meta -> meta.lore(List.of(Msg.format("<gold>Time: " + string))));
                        ItemBuilder builder = new ItemBuilder(type.getFeedType().getItemStack());
                        builder.lore(Msg.format("<white>" + user.getBarn().getItem(type.getFeedType())), 0);
                        builder.pdcSetString(Keys.animalTypeKey, type.name());
                        inventory.setItem(36, builder.build());
                        slot++;
                    }
                }
            }.runTaskTimer(Township.getInstance(), 0, 20);
            Utils.openInventory(player, inventory, uuid -> Township.getWorldManager().openWorldMenu(player), task);
        }
    }

    public void feed(Player player, AnimalType type, Inventory inventory) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        int feed = user.getBarn().getItem(type.getFeedType());
        if (feed >= 1) {
            for (int i = 0; i < 6; ++i) {
                if (!(user.getBarn().getItem(type.getFeedType()) >= 1)) return;
                if (animals.getFeed(type, i)) {
                    continue;
                }
                String string = Utils.format(Instant.now(), Instant.now().plusSeconds(type.getTimeTakesToFeed()));
                Objects.requireNonNull(inventory.getItem(11 + i)).editMeta(ItemMeta.class, meta -> meta.lore(List.of(Msg.format("<gold>Time: " + string))));
                user.getBarn().removeAmountFromItem(type.getFeedType(), 1);
                animals.setFeed(type, i, true);
                animals.setInstant(type, i, Instant.now().plusSeconds(type.getTimeTakesToFeed()));
                ItemBuilder builder = new ItemBuilder(type.getFeedType().getItemStack());
                builder.lore(Msg.format("<white>" + user.getBarn().getItem(type.getFeedType())), 0);
                builder.pdcSetString(Keys.animalTypeKey, type.name());
                inventory.setItem(36, builder.build());
            }
        }
    }

    public void pickup(Player player, AnimalType type, int slot, Inventory inventory, int actualSlot) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        if (animals.getProduct(type, slot)) {
            animals.setProduct(type, slot, false);
            animals.setFeed(type, slot, false);
            user.getBarn().addAmountToItem(type.getProductType(), 1);
            user.getLevel().addXp(type.getXpGivenOnClaim());
            inventory.clear(actualSlot);
        }
    }
}
