package me.webhead1104.towncraft.features.world.expansions;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.TileSize;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.tiles.ExpansionTile;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

public class ExpansionMenu extends TowncraftView {
    private final MutableState<Integer> slotState = initialState();
    private final State<ExpansionDataLoader.Expansion> expansionState = computedState(context -> Towncraft.getDataLoader(ExpansionDataLoader.class).get(userState.get(context).getExpansionsPurchased() + 1));

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
                context.slot(key).onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext, user.getWorld().getSection(user.getSection()), key)));
            }
        });
        for (Integer slot : TileSize.SIZE_3X3.toList(slotState.get(context))) {
            context.slot(slot).onRender(slotRenderContext -> {
                TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
                if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getCoinsNeeded()) {
                    itemStack = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
                }
                itemStack.setName(Msg.format("Expansion"));
                slotRenderContext.setItem(itemStack);
            });
        }

        TowncraftPlayer player = context.getPlayer();
        TowncraftItemStack itemStack;
        if (user.getPopulation() >= expansion.getPopulationNeeded() && user.getCoins() >= expansion.getCoinsNeeded()) {
            itemStack = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
            itemStack.setName(Msg.format("<gold>Click to buy!"));
        } else {
            itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            itemStack.setName(Msg.format("<red>You cannot purchase this!"));
        }
        itemStack.setLore(List.of(
                Utils.addResourceLine("<gold>Coins", user.getCoins(), expansion.getCoinsNeeded()),
                Utils.addResourceLine("<red>Population", user.getPopulation(), expansion.getPopulationNeeded())));
        player.getInventory().setItem(4, itemStack);

        TowncraftItemStack backButton = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
        backButton.setName(Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 85 && context.itemExists() && context.getItem().getMaterial() == TowncraftMaterial.LIME_CONCRETE) {
            User user = userState.get(context);
            ExpansionDataLoader.Expansion expansion = expansionState.get(context);
            if (user.getCoins() >= expansion.getCoinsNeeded() && user.getPopulation() >= expansion.getPopulationNeeded()) {
                user.setCoins(user.getCoins() - expansion.getCoinsNeeded());
                user.addXp(expansion.getXp());
                user.setExpansionsPurchased(user.getExpansionsPurchased() + 1);
                Instant instant = Instant.now().plus(expansion.getTime());
                TileSize.SIZE_3X3.toList(slotState.get(context)).forEach(slot -> {
                    ExpansionTile expansionTile = (ExpansionTile) user.getWorld().getSection(user.getSection()).getSlot(slot);
                    expansionTile.setInstant(instant);
                });
                context.closeForPlayer();
            }
        } else if (context.getSlot() == 81) {
            context.closeForPlayer();
        }
    }
}
