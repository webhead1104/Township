package me.webhead1104.township.features.barn;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableIntState;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.features.world.WorldMenu;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BarnMenu extends TownshipView {
    private static final Key hammerKey = Township.key("hammer");
    private static final Key nailKey = Township.key("nail");
    private static final Key paintKey = Township.key("paint");
    private final MutableIntState sellAmount = mutableState(1);
    private final MutableState<Key> sellItem = mutableState(Township.noneKey);
    private final State<Barn> barnState = computedState(context -> userState.get(context).getBarn());

    public BarnMenu() {
        super(WorldMenu.class);
    }

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title("Barn");
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        setInventoryItems(context);

        for (Key item : barnState.get(context).getItemMap().keySet()) {
            if (item.equals(Township.noneKey)) continue;
            context.availableSlot().updateOnStateChange(sellAmount).hideIf(hideContext -> barnState.get(hideContext).getItem(item) <= 0)
                    .onRender(slotRenderContext -> {
                        ItemStack stack = ItemType.get(item).getItemStack();
                        stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("%s: <yellow>%d", ItemType.get(item).getName(), barnState.get(slotRenderContext).getItem(item)));
                        stack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
                        slotRenderContext.setItem(stack);
                    }).onClick(slotClickContext -> {
                        sellItem.set(item, slotClickContext);
                        sellAmount.set(1, slotClickContext);
                        slotClickContext.update();
                    });
        }

        context.slot(48).hideIf(hideContext -> sellItem.get(hideContext).equals(Township.noneKey) || sellAmount.get(hideContext) == 1)
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    ItemStack itemStack = ItemStack.of(Material.RED_CANDLE);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to decrease the amount!"));
                    itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<blue>Currently at %d", sellAmount.get(slotRenderContext)))));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    sellAmount.decrement(slotClickContext);
                    slotClickContext.update();
                });

        context.slot(49).hideIf(hideContext -> sellItem.get(hideContext).equals(Township.noneKey))
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    ItemType.Item item = ItemType.get(sellItem.get(slotRenderContext));
                    int amount = sellAmount.get(slotRenderContext);
                    ItemStack itemStack = ItemStack.of(Material.LIME_CONCRETE);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click to sell <aqua>%d <green>of <yellow>%s <green>for <aqua>%d <gold>coins!", amount, item.getName(), item.getSellPrice() * amount));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    ItemType.Item item = ItemType.get(sellItem.get(slotClickContext));
                    int amount = sellAmount.get(slotClickContext);
                    if (barnState.get(slotClickContext).getItem(item) < amount) return;
                    barnState.get(slotClickContext).removeAmountFromItem(item, amount);
                    User user = userState.get(slotClickContext);
                    user.setCoins(user.getCoins() + item.getSellPrice() * amount);
                    sellItem.set(Township.noneKey, slotClickContext);
                    sellAmount.set(1, slotClickContext);
                    slotClickContext.openForPlayer(BarnMenu.class);
                });

        context.slot(50).hideIf(hideContext -> sellItem.get(hideContext).equals(Township.noneKey) ||
                        barnState.get(hideContext).getItem(sellItem.get(hideContext)) <= sellAmount.get(hideContext))
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    ItemStack itemStack = ItemStack.of(Material.GREEN_CANDLE);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click to increase the amount!"));
                    itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<blue>Currently at %d", sellAmount.get(slotRenderContext)))));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    sellAmount.increment(slotClickContext);
                    slotClickContext.update();
                });
    }

    @Override
    public void onUpdate(@NotNull Context context) {
        setInventoryItems(context);
    }

    private boolean canUpgrade(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int toolsNeeded = user.getBarn().getBarnUpgrade().getToolsNeeded();
        return user.getBarn().getItem(hammerKey) >= toolsNeeded && user.getBarn().getItem(nailKey) >= toolsNeeded && user.getBarn().getItem(paintKey) >= toolsNeeded;
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 81) {
            context.closeForPlayer();
            return;
        }
        if (context.getSlot() == 89) {
            if (canUpgrade(context.getPlayer())) {
                User user = Township.getUserManager().getUser(context.getPlayer().getUniqueId());
                BarnUpgrade newUpgrade = BarnUpdateDataLoader.get(user.getBarn().getBarnUpgrade().getId());
                if (newUpgrade == null) {
                    throw new NullPointerException("barn upgrade not found");
                }
                user.getBarn().setBarnUpgrade(newUpgrade);
                user.getBarn().removeAmountFromItem(hammerKey, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(nailKey, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(paintKey, newUpgrade.getToolsNeeded());
                context.openForPlayer(BarnMenu.class);
            }
        }
    }

    private void setInventoryItems(Context context) {
        Barn barn = barnState.get(context);
        Player player = context.getPlayer();
        ItemStack storage;
        if (barn.getBarnUpgrade().getBarnStorage() > barn.getStorage()) {
            storage = ItemStack.of(Material.LIME_CONCRETE);
            storage.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>%d/%d", barn.getStorage(), barn.getBarnUpgrade().getBarnStorage()));
        } else {
            storage = ItemStack.of(Material.RED_CONCRETE);
            storage.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Full! %d/%d", barn.getStorage(), barn.getBarnUpgrade().getBarnStorage()));
        }
        ItemLore.Builder storageLore = ItemLore.lore();
        barn.getItemMap().forEach((key, value) -> {
            if (value != 0) {
                storageLore.addLine(Msg.format("<white>%s: <yellow>%d", ItemType.get(key).getName(), value));
            }
        });
        storage.setData(DataComponentTypes.LORE, storageLore.build());
        player.getInventory().setItem(1, storage);

        ItemStack upgrade;
        ItemLore.Builder upgradeLore = ItemLore.lore();
        if (canUpgrade(player)) {
            upgrade = ItemStack.of(Material.LIME_CONCRETE);
            upgradeLore.addLine(Msg.format("<green>Click to upgrade!"));
        } else {
            upgrade = ItemStack.of(Material.RED_CONCRETE);
            upgradeLore.addLine(Msg.format("<red>You need to get more materials to upgrade!"));
        }
        int toolsNeeded = barn.getBarnUpgrade().getToolsNeeded();
        upgradeLore.addLine(Utils.addResourceLine("Hammers", barn.getItem(hammerKey), toolsNeeded));
        upgradeLore.addLine(Utils.addResourceLine("Nails", barn.getItem(nailKey), toolsNeeded));
        upgradeLore.addLine(Utils.addResourceLine("Paint buckets", barn.getItem(paintKey), toolsNeeded));

        upgrade.setData(DataComponentTypes.LORE, upgradeLore.build());
        upgrade.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Barn level: %d", barn.getBarnUpgrade().getId()));
        player.getInventory().setItem(8, upgrade);

        ItemStack backButton = ItemStack.of(Material.BARRIER);
        backButton.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }
}
