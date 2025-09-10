package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldMenu extends View {
    private final State<Integer> sectionState = initialState();
    private final MutableState<Boolean> openConfirmClose = mutableState(true);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("World Menu"));
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
        Township.getUserManager().getUser(context.getPlayer().getUniqueId()).setSection(sectionState.get(context));
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (openConfirmClose.get(context)) {
                Township.getViewFrame().open(ConfirmCloseMenu.class, context.getPlayer());
            }
        }, 1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        World world = user.getWorld();
        world.getSection(sectionState.get(context)).getSlotMap().forEach((key, tile) -> context.slot(key).updateOnClick().onRender(slotRenderContext -> slotRenderContext.setItem(tile.render(slotRenderContext))).onClick(clickContext -> {
            if (tile.onClick(clickContext)) {
                openConfirmClose.set(false, clickContext);
            }
        }));

        if (Township.getWorldManager().canMoveRight(sectionState.get(context))) {
            ItemStack stack = ItemStack.of(Material.ARROW);
            stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<dark_green>Click to scroll right!"));
            player.getInventory().setItem(23, stack);
        }

        if (Township.getWorldManager().canMoveDown(sectionState.get(context))) {
            ItemStack stack = ItemStack.of(Material.ARROW);
            stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<dark_green>Click to scroll down!"));
            player.getInventory().setItem(31, stack);
        }

        if (Township.getWorldManager().canMoveLeft(sectionState.get(context))) {
            ItemStack stack = ItemStack.of(Material.ARROW);
            stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<dark_green>Click to scroll left!"));
            player.getInventory().setItem(21, stack);
        }

        if (Township.getWorldManager().canMoveUp(sectionState.get(context))) {
            ItemStack stack = ItemStack.of(Material.ARROW);
            stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<dark_green>Click to scroll up!"));
            player.getInventory().setItem(13, stack);
        }

        ItemStack profileItemStack = ItemStack.of(Material.LIGHT_BLUE_CONCRETE);
        profileItemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>%s", user.getTownName()));
        player.getInventory().setItem(22, profileItemStack);

        ItemStack levelAndPopStack = ItemStack.of(Material.BLUE_CONCRETE);
        levelAndPopStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<aqua>Level %d", user.getLevel()));
        if (Township.getLevelManager().getLevelMap().containsKey(user.getLevel() + 1)) {
            levelAndPopStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<aqua>Xp %d", user.getXp()),
                    Msg.format(Township.getLevelManager().getProgressBar(player)),
                    Msg.format("<red>Population %d", user.getPopulation())
            )));
        } else {
            levelAndPopStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Msg.format("<dark_red>You have reached the max level!"),
                    Msg.format("<red>Population " + user.getPopulation())
            )));
        }
        player.getInventory().setItem(9, levelAndPopStack);

        ItemStack coinsAndCashStack = ItemStack.of(Material.GOLD_BLOCK);
        coinsAndCashStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<yellow>Coins %d", user.getCoins()));
        coinsAndCashStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>Cash %d", user.getCash()))));
        player.getInventory().setItem(17, coinsAndCashStack);

        ItemStack buildingMenuStack = ItemStack.of(Material.YELLOW_CONCRETE);
        buildingMenuStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Build Menu"));
        player.getInventory().setItem(8, buildingMenuStack);
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (context.isOnEntityContainer()) {
            if (context.getClickedSlot() == 68 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) + 1);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 76 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) + 8);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 66 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) - 1);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 58 && context.getItem() != null) {
                context.openForPlayer(WorldMenu.class, sectionState.get(context) - 8);
                openConfirmClose.set(false, context);
            } else if (context.getClickedSlot() == 89 && context.getItem() != null) {
                context.openForPlayer(BuildMenu.class);
                openConfirmClose.set(false, context);
            }
        }
    }
}
