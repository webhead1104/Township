package me.webhead1104.township.features.factories;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.Factories;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FactoryMenu extends View {
    private final State<FactoryType> factoryType = initialState();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        FactoryType factoryType = this.factoryType.get(context);
        context.modifyConfig().title(factoryType.getMenuTitle());
        Player player = context.getPlayer();
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> Township.getWorldManager().openWorldMenu(context.getPlayer()), 1);
    }

    @Override
    public void onFirstRender(RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        FactoryType factoryType = this.factoryType.get(context);
        Factories.Factory factory = user.getFactories().getFactory(factoryType);

        int recipeSlot = 45;
        for (RecipeType recipeType : factoryType.getRecipes()) {
            context.slot(recipeSlot++).onRender(slotRenderContext -> {
                ItemStack stack = recipeType.getItemStack();
                stack.setData(DataComponentTypes.LORE, calculateLore(recipeType, user.getBarn()));
                slotRenderContext.setItem(stack);
            }).updateOnClick().onClick(slotClickContext -> {
                if (!factory.canAddWaitingOrWorkingOn() || !recipeType.hasRequiredItems(user.getBarn())) return;
                recipeType.getRecipeItems().forEach((key, value) -> user.getBarn().removeAmountFromItem(key, value));
                if (factory.canSetWorkingOn()) {
                    factory.setWorkingOn(recipeType);
                    factory.setInstant(Instant.now().plusSeconds(recipeType.getTime().getSeconds()));
                } else if (factory.canAddWaiting()) {
                    factory.addWaiting(recipeType);
                }
                context.update();
            });
        }

        int waitingSlot = 29;
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            context.slot(waitingSlot++).onRender(slotRenderContext -> {
                if (factory.getWaiting(finalI).equals(RecipeType.NONE)) {
                    ItemStack stack = ItemStack.of(Material.HOPPER);
                    stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Nothing is being made right now"));
                    stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<grey>Maybe you should make something!"))));
                    slotRenderContext.setItem(stack);
                    return;
                }
                ItemStack stack = factory.getWaiting(finalI).getItemStack();
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<dark_green>Waiting..."))));
                slotRenderContext.setItem(stack);
            });
        }

        context.slot(27).onRender(slotRenderContext -> {
            if (!factory.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(factory.getInstant().minusSeconds(1))) {
                factory.setInstant(Instant.EPOCH);
                if (factory.canAddCompleted()) {
                    factory.addCompleted(factory.getWorkingOn().getResultItemType());
                    factory.setWorkingOn(RecipeType.NONE);
                    if (factory.hasWaiting()) {
                        RecipeType recipeType = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipeType);
                        factory.setInstant(Instant.now().plusSeconds(recipeType.getTime().getSeconds()));
                    }
                }
                context.update();
            }
            if (factory.getWorkingOn().equals(RecipeType.NONE)) {
                ItemStack stack = ItemStack.of(Material.RED_CANDLE);
                stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Nothing is being made right now"));
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<grey>Maybe you should make something!"))));
                slotRenderContext.setItem(stack);
                return;
            }
            if (factory.getInstant().equals(Instant.EPOCH)) {
                ItemStack stack = factory.getWorkingOn().getItemStack();
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<red>Your production queue is full!"))));
                slotRenderContext.setItem(stack);
                return;
            }
            ItemStack stack = factory.getWorkingOn().getItemStack();
            stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), factory.getInstant())))));
            slotRenderContext.setItem(stack);
        });

        int completedSlot = 12;
        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            context.slot(completedSlot++).onRender(slotRenderContext -> {
                ItemType itemType = factory.getCompleted(finalI);
                if (itemType.equals(ItemType.NONE)) {
                    ItemStack stack = ItemStack.of(Material.CHEST);
                    stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Nothing is being made right now"));
                    stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<grey>Maybe you should make something!"))));
                    slotRenderContext.setItem(stack);
                    return;
                }
                ItemStack stack = itemType.getItemStack();
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>Click to claim!"))));
                slotRenderContext.setItem(stack);
            }).onClick(slotClickContext -> {
                if (factory.getCompleted(finalI).equals(ItemType.NONE)) return;
                user.getBarn().addAmountToItem(factory.getCompleted(finalI), 1);
                user.addXp(factory.getCompleted(finalI).getXpGiven());
                factory.setCompleted(finalI, ItemType.NONE);
                if (factory.getInstant().equals(Instant.EPOCH) && !factory.getWorkingOn().equals(RecipeType.NONE)) {
                    factory.addCompleted(factory.getWorkingOn().getResultItemType());
                    factory.setWorkingOn(RecipeType.NONE);
                    if (factory.hasWaiting()) {
                        RecipeType recipeType = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipeType);
                        factory.setInstant(Instant.now().plusSeconds(recipeType.getTime().getSeconds()));
                    }
                }
                context.update();
            });
        }
    }

    private ItemLore calculateLore(RecipeType recipeType, Barn barn) {
        List<Component> lore = new ArrayList<>();
        recipeType.getRecipeItems().forEach((itemType, value) -> {
            if (barn.getItem(itemType) >= value) {
                lore.add(Msg.format("<white>%s: <green>%d/%d", Utils.thing2(itemType.name()), barn.getItem(itemType), value));
            } else {
                lore.add(Msg.format("<white>%s: <red>%d/%d", Utils.thing2(itemType.name()), barn.getItem(itemType), value));
            }
        });
        return ItemLore.lore(lore);
    }
}
