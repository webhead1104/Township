package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@NoArgsConstructor
public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            ItemBuilder builder = new ItemBuilder(item);
            if (builder.getMeta() != null && builder.pdcHas(Keys.townshipIdKey)) {
                event.setCancelled(true);
                String itemID = builder.pdcGetString(Keys.townshipIdKey);
                Player player = (Player) event.getWhoClicked();
                switch (itemID.toLowerCase()) {
                    case "world_arrow" ->
                            Township.getWorldManager().openWorldMenu(player, builder.pdcGetInt(Keys.newPageKey));
                    case "township", "back_button" ->
                            Township.getUserManager().getPlayerCloseHandler(player.getUniqueId()).accept(player.getUniqueId());
                    case "train" -> Township.getTrainManager().openMenu(player);
                    case "train_buy" ->
                            Township.getTrainManager().purchaseTrain(player, builder.pdcGetInt(Keys.trainKey));
                    case "train_collect" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(Keys.itemAmountKey);
                        int train = builder.pdcGetInt(Keys.trainKey);
                        int car = builder.pdcGetInt(Keys.trainCarKey);
                        Township.getTrainManager().collectItem(player, itemType, amount, train, car);
                    }
                    case "train_give" -> {
                        ItemType itemType = ItemType.valueOf(builder.pdcGetString(Keys.itemTypeKey).toUpperCase());
                        int amount = builder.pdcGetInt(Keys.itemAmountKey);
                        int train = builder.pdcGetInt(Keys.trainKey);
                        int car = builder.pdcGetInt(Keys.trainCarKey);
                        Township.getTrainManager().giveItem(player, itemType, amount, train, car);
                    }
                }
            }
        } else {
            if (event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) ||
                    event.getAction().equals(InventoryAction.DROP_ONE_CURSOR) ||
                    event.getAction().equals(InventoryAction.NOTHING)) {
                event.setCancelled(true);
            } else {
                ItemBuilder builder = new ItemBuilder(Objects.requireNonNull(event.getInventory()).getItem(event.getSlot()));
                if (builder.getMeta() != null && builder.pdcHas(Keys.townshipIdKey)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
