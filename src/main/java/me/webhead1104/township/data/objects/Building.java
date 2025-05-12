package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.TileSize;
import me.webhead1104.township.data.interfaces.Price;
import me.webhead1104.township.utils.ItemBuilder;
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

    public Building(int levelNeeded, int populationIncrease, int maxPopulationIncrease, Price price, @Nullable ConstructionMaterials materials, @Nullable Duration timeToBuild, int xpGiven, TileSize size, String ID) {
        this.levelNeeded = levelNeeded;
        this.populationIncrease = populationIncrease;
        this.maxPopulationIncrease = maxPopulationIncrease;
        this.price = price;
        this.materials = materials;
        this.timeToBuild = timeToBuild;
        this.xpGiven = xpGiven;
        this.size = size;
        this.ID = ID.toUpperCase();
    }

    public ItemStack getItemStack(String buildMenuName, Player player) {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD, "build_menu_" + buildMenuName.toLowerCase() + "_" + ID.toLowerCase());
        Component populationLine;
        if (populationIncrease == 0 && maxPopulationIncrease > 0) {
            populationLine = Msg.format(String.format("<red>+%s Max Population", maxPopulationIncrease));
        } else {
            populationLine = Msg.format(String.format("<red>+%s Population", populationIncrease));
        }
        builder.displayName(Msg.format("<aqua>" + Utils.thing2(ID)));
        builder.lore(List.of(
                Component.empty(),
                Msg.format("<green>Loading..."),
                Component.empty(),
                populationLine,
                Component.empty(),
                Msg.format("<blue>Level needed<white>: %s<aqua>/<white>%s", levelNeeded, Township.getUserManager().getUser(player.getUniqueId()).getLevel().getLevel()),
                price.neededComponent(player)
        ));
        return builder.build();
    }
}
