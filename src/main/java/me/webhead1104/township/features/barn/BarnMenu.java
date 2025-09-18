package me.webhead1104.township.features.barn;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.features.world.WorldMenu;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BarnMenu extends TownshipView {
    private final MutableState<Boolean> openWorldMenu = mutableState(true);
    private final MutableState<ItemType> sellItem = mutableState(ItemType.NONE);
    private final MutableState<Integer> sellAmount = mutableState(1);

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
    public void onOpen(@NotNull OpenContext context) {
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Barn barn = user.getBarn();
        int i = 0;
        for (ItemType itemType : ItemType.values()) {
            if (itemType.equals(ItemType.NONE)) continue;
            if (barn.getItem(itemType) == 0) continue;
            context.slot(i++).updateOnClick().onRender(slotRenderContext -> {
                ItemStack stack = itemType.getItemStack();
                stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("%s: <yellow>%d", Utils.thing2(itemType.name()), barn.getItem(itemType)));
                stack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
                slotRenderContext.setItem(stack);
            }).onClick(slotClickContext -> {
                if (barn.getItem(itemType) == 0) return;
                sellItem.set(itemType, slotClickContext);
                sellAmount.set(1, slotClickContext);
                context.update();
            });
        }

        context.slot(48).onRender(slotRenderContext -> {
            ItemType itemType = sellItem.get(slotRenderContext);
            int amount = sellAmount.get(slotRenderContext);
            if (itemType == ItemType.NONE) return;
            if (amount != 1) {
                ItemStack itemStack = ItemStack.of(Material.RED_CANDLE);
                itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to decrease the amount!"));
                itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<blue>Currently at %d", amount))));
                slotRenderContext.setItem(itemStack);
            }
        }).onClick(slotClickContext -> {
            ItemType itemType = sellItem.get(slotClickContext);
            int amount = sellAmount.get(slotClickContext);
            if (itemType == ItemType.NONE) return;
            if (amount == 1) return;
            sellAmount.set(amount - 1, slotClickContext);
            slotClickContext.update();
        });

        context.slot(49).onRender(slotRenderContext -> {
            ItemType itemType = sellItem.get(slotRenderContext);
            int amount = sellAmount.get(slotRenderContext);
            if (itemType == ItemType.NONE) return;
            ItemStack itemStack = ItemStack.of(Material.LIME_CONCRETE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click to sell <aqua>%d <green>of <yellow>%s <green>for <aqua>%d <gold>coins!", amount, Utils.thing2(itemType.name()), itemType.getSellPrice() * amount));
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            ItemType itemType = sellItem.get(slotClickContext);
            int amount = sellAmount.get(slotClickContext);
            if (itemType == ItemType.NONE) return;
            if (barn.getItem(itemType) < amount) return;
            barn.removeAmountFromItem(itemType, amount);
            user.setCoins(user.getCoins() + itemType.getSellPrice() * amount);
            sellItem.set(ItemType.NONE, slotClickContext);
            sellAmount.set(1, slotClickContext);
            slotClickContext.openForPlayer(BarnMenu.class);
        });

        context.slot(50).onRender(slotRenderContext -> {
            ItemType itemType = sellItem.get(slotRenderContext);
            int amount = sellAmount.get(slotRenderContext);
            if (itemType == ItemType.NONE) return;
            if (barn.getItem(itemType) > amount) {
                ItemStack itemStack = ItemStack.of(Material.GREEN_CANDLE);
                itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click to increase the amount!"));
                itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<blue>Currently at %d", amount))));
                slotRenderContext.setItem(itemStack);
            }
        }).onClick(slotClickContext -> {
            ItemType itemType = sellItem.get(slotClickContext);
            int amount = sellAmount.get(slotClickContext);
            if (itemType == ItemType.NONE) return;
            if (barn.getItem(itemType) <= amount) return;
            sellAmount.set(amount + 1, slotClickContext);
            slotClickContext.update();
        });

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
                storageLore.addLine(Msg.format("<white>%s: <yellow>%d", Utils.thing2(key.name()), value));
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
        if (barn.getItem(ItemType.HAMMER) >= barn.getBarnUpgrade().getToolsNeeded()) {
            upgradeLore.addLine(Msg.format("<white>Hammers: <green>%d/%d", barn.getItem(ItemType.HAMMER), barn.getBarnUpgrade().getToolsNeeded()));

        } else {
            upgradeLore.addLine(Msg.format("<white>Hammers: <red>%d/%d", barn.getItem(ItemType.HAMMER), barn.getBarnUpgrade().getToolsNeeded()));
        }

        if (barn.getItem(ItemType.NAIL) >= barn.getBarnUpgrade().getToolsNeeded()) {
            upgradeLore.addLine(Msg.format("<white>Nails: <green>%d/%d", barn.getItem(ItemType.NAIL), barn.getBarnUpgrade().getToolsNeeded()));

        } else {
            upgradeLore.addLine(Msg.format("<white>Nails: <red>%d/%d", barn.getItem(ItemType.NAIL), barn.getBarnUpgrade().getToolsNeeded()));
        }

        if (barn.getItem(ItemType.PAINT) >= barn.getBarnUpgrade().getToolsNeeded()) {
            upgradeLore.addLine(Msg.format("<white>Paint buckets: <green>%d/%d", barn.getItem(ItemType.PAINT), barn.getBarnUpgrade().getToolsNeeded()));

        } else {
            upgradeLore.addLine(Msg.format("<white>Paint buckets: <red>%d/%d", barn.getItem(ItemType.HAMMER), barn.getBarnUpgrade().getToolsNeeded()));
        }
        upgrade.setData(DataComponentTypes.LORE, upgradeLore.build());
        upgrade.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Barn level: %d", barn.getBarnUpgrade().getId()));
        player.getInventory().setItem(8, upgrade);

        ItemStack backButton = ItemStack.of(Material.BARRIER);
        backButton.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }

    public boolean canUpgrade(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int toolsNeeded = user.getBarn().getBarnUpgrade().getToolsNeeded();
        return user.getBarn().getItem(ItemType.HAMMER) >= toolsNeeded && user.getBarn().getItem(ItemType.NAIL) >= toolsNeeded && user.getBarn().getItem(ItemType.PAINT) >= toolsNeeded;
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
                user.getBarn().setBarnUpgrade(newUpgrade);
                user.getBarn().removeAmountFromItem(ItemType.HAMMER, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(ItemType.NAIL, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(ItemType.PAINT, newUpgrade.getToolsNeeded());
                context.openForPlayer(BarnMenu.class);
            }
        }
    }
}
