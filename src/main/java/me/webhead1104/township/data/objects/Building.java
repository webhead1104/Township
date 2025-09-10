package me.webhead1104.township.data.objects;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.TileSize;
import me.webhead1104.township.price.Price;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Building {
    private int levelNeeded;
    private int populationNeeded;
    private int populationIncrease;
    private int maxPopulationIncrease;
    private Price price;
    @Nullable
    private ConstructionMaterials materials;
    @Nullable
    private Duration timeToBuild;
    private Tile tile;
    private int xpGiven;
    private TileSize size;
    private String ID;

    public ItemStack getItemStack(Player player) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Utils.thing2(ID)));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Msg.format("<green>Loading..."));
        if (populationIncrease == 0 && maxPopulationIncrease > 0) {
            lore.add(Component.empty());
            lore.add(Msg.format(String.format("<red>+%s Max Population", maxPopulationIncrease)));
        } else if (populationIncrease > 0 && maxPopulationIncrease == 0) {
            lore.add(Component.empty());
            lore.add(Msg.format(String.format("<red>+%s Population", populationIncrease)));
        }
        if (Township.getUserManager().getUser(player.getUniqueId()).getLevel() >= levelNeeded) {
            lore.add(Component.empty());
            lore.add(Msg.format("<blue>Level needed<white>: <green>%s/%s", Township.getUserManager().getUser(player.getUniqueId()).getLevel(), levelNeeded));
        } else {
            lore.add(Component.empty());
            lore.add(Msg.format("<blue>Level needed<white>: <red>%s/%s", Township.getUserManager().getUser(player.getUniqueId()).getLevel(), levelNeeded));
        }
        if (populationNeeded > 0) {
            lore.add(Component.empty());
            if (Township.getUserManager().getUser(player.getUniqueId()).getPopulation() >= populationNeeded) {
                lore.add(Msg.format("<red>Population needed<white>: <green>%s/%s", Township.getUserManager().getUser(player.getUniqueId()).getPopulation(), populationNeeded));
            } else {
                lore.add(Msg.format("<red>Population needed<white>: <red>%s/%s", Township.getUserManager().getUser(player.getUniqueId()).getPopulation(), populationNeeded));
            }
        }
        lore.add(Component.empty());
        lore.add(price.getComponent(player));

        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        return itemStack;
    }
}
