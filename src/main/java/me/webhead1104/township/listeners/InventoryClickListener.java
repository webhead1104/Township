
package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.*;
import me.webhead1104.township.data.objects.Building;
import me.webhead1104.township.data.objects.Expansion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@NoArgsConstructor
public class InventoryClickListener implements Listener {

//    @EventHandler
//    public void onClick(InventoryClickEvent event) {
//        ItemStack item = event.getCurrentItem();
//        if (item != null) {
//            ItemBuilder builder = new ItemBuilder(item);
//            if (builder.pdcHas(Keys.townshipIdKey)) {
//                event.setCancelled(true);
//                String itemID = builder.pdcGetString(Keys.townshipIdKey);
//                Player player = (Player) event.getWhoClicked();
//                switch (itemID.toLowerCase()) {
//
//                    case "home_button" -> Township.getWorldManager().openWorldMenu(player);
//                    case "build_menu" -> Township.getBuildManager().openMenu(player);
//                    case "build_arrow" ->
//                            Township.getBuildManager().openMenu(player, BuildMenuType.valueOf(builder.pdcGetString(Keys.buildMenuTypeKey).toUpperCase()), builder.pdcGetInt(Keys.newPageKey));
//                    case "build_place" -> {
//                        BuildingType buildingType = BuildingType.valueOf(builder.pdcGetString(Keys.typeKey).toUpperCase());
//                        Township.getBuildManager().placeMenu(player, buildingType, buildingType.getBuildings().get(builder.pdcGetInt(Keys.slot)), builder.pdcGetInt(Keys.slot), event.getSlot(), builder.pdcGetInt(Keys.section));
//                    }
//                    case "build_world_arrow" -> {
//                        BuildingType buildingType = BuildingType.valueOf(builder.pdcGetString(Keys.typeKey).toUpperCase());
//                        Township.getBuildManager().placeMenu(player, buildingType, buildingType.getBuildings().get(builder.pdcGetInt(Keys.slot)), builder.pdcGetInt(Keys.slot), event.getSlot(), builder.pdcGetInt(Keys.newPageKey));
//                    }
//                    case "build_confirm" -> {
//                        BuildingType buildingType = BuildingType.valueOf(builder.pdcGetString(Keys.typeKey).toUpperCase());
//                        Building building = buildingType.getBuildings().get(builder.pdcGetInt(Keys.slot));
//                        Township.getBuildManager().confirm(player, buildingType, building, builder.pdcGetInt(Keys.slot), event.getSlot(), builder.pdcGetInt(Keys.section));
//                    }
//                }
//                for (BuildMenuType buildMenuType : BuildMenuType.values()) {
//                    if (itemID.equals("build_menu_" + buildMenuType.name().toLowerCase())) {
//                        Township.getBuildManager().openMenu(player, buildMenuType);
//                        return;
//                    }
//                    for (BuildingType value : BuildingType.values()) {
//                        if (itemID.equals("build_menu_" + buildMenuType.name().toLowerCase() + "_" + value.name().toLowerCase())) {
//                            Township.getBuildManager().purchase(player, value, event.getInventory(), event.getSlot());
//                        }
//                    }
//                }
//            }
//        } else {
//            if (event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) ||
//                    event.getAction().equals(InventoryAction.DROP_ONE_CURSOR) ||
//                    event.getAction().equals(InventoryAction.NOTHING)) {
//                event.setCancelled(true);
//            } else {
//                ItemBuilder builder = new ItemBuilder(Objects.requireNonNull(event.getInventory()).getItem(event.getSlot()));
//                if (builder.pdcHas(Keys.townshipIdKey)) {
//                    event.setCancelled(true);
//                }
//            }
//        }
//    }
}