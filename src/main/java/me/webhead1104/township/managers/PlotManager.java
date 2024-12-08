package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class PlotManager {

    public void openMenu(Player player) {
        player.sendMessage("opened menu");
        Township.getWorldManager().openWorldMenu(player);
        player.getInventory().clear();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        AtomicInteger i = new AtomicInteger(2);
        for (PlotType type : PlotType.values()) {
            if (type.equals(PlotType.NONE)) continue;
            if (!(user.getLevel().getLevel() >= type.getLevelNeeded())) continue;
            ItemBuilder itemBuilder = new ItemBuilder(type.getItemType().getItemStack());
            itemBuilder.id(type.getId().toLowerCase() + "_type_select");
            String price = type.getPrice() == 0 ? "FREE!" : String.valueOf(type.getPrice());
            if (user.getCoins() >= type.getPrice()) {
                itemBuilder.lore(Msg.format("<blue>Coins needed <green>" + user.getCoins() + "/" + price));
            } else itemBuilder.lore(Msg.format("<red>Coins needed " + user.getCoins() + "/" + price));
            player.getInventory().setItem(i.getAndIncrement(), itemBuilder.build());
        }
        player.getInventory().setItem(0, MenuItems.backButton);
    }

    public void selectCropType(PlotType plotType, Player player) {
        if (Township.getUserManager().getUser(player.getUniqueId()).getCoins() >= plotType.getPrice()) {
            ItemBuilder builder = new ItemBuilder(plotType.getItemType().getItemStack());
            builder.id(plotType.getId() + "_type_selected");
            player.setItemOnCursor(builder.build());
        }
    }

    public void plant(Player player, ItemStack clickedItem, ItemStack cursorItem) {
        player.setItemOnCursor(ItemStack.empty());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        ItemBuilder builder = new ItemBuilder(clickedItem);
        Plot plot = Plot.fromJson(builder.pdcGetString(ItemBuilder.plotDataKey));
        PlotType cursorPlotType = PlotType.valueOf(new ItemBuilder(cursorItem).getId().split("_")[0].toUpperCase());
        if (user.getCoins() >= cursorPlotType.getPrice()) {
            user.setCoins(user.getCoins() - cursorPlotType.getPrice());
            plot.setPlotType(cursorPlotType);
            user.getWorld().getSection(plot.getSection()).getSlot(plot.getSlot()).setPlot(plot);
            openMenu(player);
            player.setItemOnCursor(cursorItem);
        }
    }

    public void harvest(Player player, ItemStack clickedItem) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        ItemBuilder builder = new ItemBuilder(clickedItem);
        Plot plot = Plot.fromJson(builder.pdcGetString(ItemBuilder.plotDataKey));
        user.getBarn().addAmountToItem(plot.getPlotType().getItemType(), 1);
        user.getLevel().addXp(plot.getPlotType().getXpGiven());
        plot.setPlotType(PlotType.NONE);
        user.getWorld().getSection(plot.getSection()).getSlot(plot.getSlot()).setPlot(plot);
        Township.getWorldManager().openWorldMenu(player);
    }
}
