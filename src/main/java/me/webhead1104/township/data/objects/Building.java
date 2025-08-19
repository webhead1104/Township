package me.webhead1104.township.data.objects;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.TileSize;
import me.webhead1104.township.price.Price;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Building {
    private int levelNeeded;
    private int populationIncrease;
    private int maxPopulationIncrease;
    private Price price;
    @Nullable
    private ConstructionMaterials materials;
    @Nullable
    private Duration timeToBuild;
    private int xpGiven;
    private TileSize size;
    private String ID;

    public ItemStack getItemStack(Player player) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Utils.thing2(ID)));

        Component populationLine;
        if (populationIncrease == 0 && maxPopulationIncrease > 0) {
            populationLine = Msg.format(String.format("<red>+%s Max Population", maxPopulationIncrease));
        } else {
            populationLine = Msg.format(String.format("<red>+%s Population", populationIncrease));
        }

        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.empty(), Msg.format("<green>Loading..."), Component.empty(), populationLine, Component.empty(), Msg.format("<blue>Level needed<white>: %s<aqua>/<white>%s", levelNeeded, Township.getUserManager().getUser(player.getUniqueId()).getLevel()), price.component(player))));
        return itemStack;
    }
}
