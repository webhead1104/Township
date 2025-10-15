package me.webhead1104.township.features.world.expansions;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.TileSize;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.tiles.ExpansionTile;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

public class ExpansionMenu extends TownshipView {
    private final MutableState<Integer> slotState = initialState();
    private final State<ExpansionDataLoader.Expansion> expansionState = computedState(context -> Township.getDataLoader(ExpansionDataLoader.class).get(userState.get(context).getExpansionsPurchased() + 1));

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Expansion Menu"));
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        User user = userState.get(context);
        ExpansionDataLoader.Expansion expansion = expansionState.get(context);

        user.getWorld().getSection(user.getSection()).getSlotMap().forEach((key, tile) -> {
            if (!TileSize.SIZE_3X3.toList(slotState.get(context)).contains(key)) {
                context.slot(key).onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext)));
            }
        });
        for (Integer slot : TileSize.SIZE_3X3.toList(slotState.get(context))) {
            context.slot(slot).onRender(slotRenderContext -> {
                ItemStack itemStack = ItemStack.of(Material.RED_CONCRETE);
                if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getCoinsNeeded()) {
                    itemStack = itemStack.withType(Material.LIME_CONCRETE);
                }
                itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Expansion"));
                slotRenderContext.setItem(itemStack);
            });
        }

        Player player = context.getPlayer();
        ItemStack itemStack;
        if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getCoinsNeeded()) {
            itemStack = ItemStack.of(Material.LIME_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<gold>Click to buy!"));
        } else {
            itemStack = ItemStack.of(Material.RED_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>You cannot purchase this!"));
        }
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Utils.addResourceLine("<gold>Coins", user.getCoins(), expansion.getCoinsNeeded()),
                Utils.addResourceLine("<red>Population", user.getPopulation(), expansion.getPopulationNeeded()))));
        player.getInventory().setItem(4, itemStack);

        ItemStack backButton = ItemStack.of(Material.BARRIER);
        backButton.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 85 && context.getItem() != null && context.getItem().getType() == Material.LIME_CONCRETE) {
            User user = userState.get(context);
            ExpansionDataLoader.Expansion expansion = expansionState.get(context);
            if (user.getCoins() >= expansion.getCoinsNeeded() && user.getPopulation() >= expansion.getPopulationNeeded()) {
                user.setCoins(user.getCoins() - expansion.getCoinsNeeded());
                user.addXp(expansion.getXp());
                user.setExpansionsPurchased(user.getExpansionsPurchased() + 1);
                TileSize.SIZE_3X3.toList(slotState.get(context)).forEach(slot -> {
                    ExpansionTile expansionTile = (ExpansionTile) user.getWorld().getSection(user.getSection()).getSlot(slot);
                    expansionTile.setInstant(Instant.now().plus(expansion.getTime()));
                });
                context.closeForPlayer();
            }
        } else if (context.getSlot() == 81) {
            context.closeForPlayer();
        }
    }
}
