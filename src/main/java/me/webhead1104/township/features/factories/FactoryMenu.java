package me.webhead1104.township.features.factories;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.data.objects.Factories;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.menus.TownshipView;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FactoryMenu extends TownshipView {
    private final State<Key> keyState = initialState();
    private final State<FactoryType.Factory> factoryState = computedState(context -> Township.getDataLoader(FactoryType.class).get(keyState.get(context)));

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        FactoryType.Factory factory = this.factoryState.get(context);
        context.modifyConfig().title(factory.getMenuTitle());
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        FactoryType.Factory factoryData = this.factoryState.get(context);
        Factories.Factory factory = user.getFactories().getFactory(factoryData.key());

        int recipeSlot = 45;
        for (RecipeType.Recipe recipe : factoryData.getRecipes()) {
            context.slot(recipeSlot++).onRender(slotRenderContext -> {
                ItemStack stack = recipe.getResult().getItemStack();
                stack.setData(DataComponentTypes.LORE, calculateLore(recipe, user.getBarn()));
                slotRenderContext.setItem(stack);
            }).updateOnClick().onClick(slotClickContext -> {
                if (!factory.canAddWaitingOrWorkingOn() || !recipe.hasRequiredItems(user.getBarn())) return;
                recipe.getIngredients().forEach((item, value) -> user.getBarn().removeAmountFromItem(item, value));
                if (factory.canSetWorkingOn()) {
                    factory.setWorkingOn(recipe.getKey());
                    factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                } else if (factory.canAddWaiting()) {
                    factory.addWaiting(recipe.getKey());
                }
                context.update();
            });
        }

        int waitingSlot = 29;
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            context.slot(waitingSlot++).onRender(slotRenderContext -> {
                if (factory.getWaiting(finalI).equals(Township.noneKey)) {
                    ItemStack stack = ItemStack.of(Material.HOPPER);
                    stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Nothing is being made right now"));
                    stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<grey>Maybe you should make something!"))));
                    slotRenderContext.setItem(stack);
                    return;
                }
                ItemStack stack = factory.getWaiting(finalI).getResult().getItemStack();
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<dark_green>Waiting..."))));
                slotRenderContext.setItem(stack);
            });
        }

        context.slot(27).onRender(slotRenderContext -> {
            if (!factory.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(factory.getInstant().minusSeconds(1))) {
                factory.setInstant(Instant.EPOCH);
                if (factory.canAddCompleted()) {
                    factory.addCompleted(factory.getWorkingOn().getResult().key());
                    factory.setWorkingOn(Township.noneKey);
                    if (factory.hasWaiting()) {
                        RecipeType.Recipe recipe = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipe.getKey());
                        factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                    }
                }
                context.update();
            }
            if (factory.getWorkingOn().equals(Township.noneKey)) {
                ItemStack stack = ItemStack.of(Material.RED_CANDLE);
                stack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Nothing is being made right now"));
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<grey>Maybe you should make something!"))));
                slotRenderContext.setItem(stack);
                return;
            }
            if (factory.getInstant().equals(Instant.EPOCH)) {
                ItemStack stack = factory.getWorkingOn().getResult().getItemStack();
                stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<red>Your production queue is full!"))));
                slotRenderContext.setItem(stack);
                return;
            }
            ItemStack stack = factory.getWorkingOn().getResult().getItemStack();
            stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), factory.getInstant())))));
            slotRenderContext.setItem(stack);
        });

        int completedSlot = 12;
        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            context.slot(completedSlot++).onRender(slotRenderContext -> {
                ItemType.Item itemType = factory.getCompleted(finalI);
                if (itemType.equals(Township.noneKey)) {
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
                if (factory.getCompleted(finalI).equals(Township.noneKey)) return;
                user.getBarn().addAmountToItem(factory.getCompleted(finalI), 1);
                user.addXp(factory.getCompleted(finalI).getXpGiven());
                factory.setCompleted(finalI, Township.noneKey);
                if (factory.getInstant().equals(Instant.EPOCH) && !factory.getWorkingOn().equals(Township.noneKey)) {
                    factory.addCompleted(factory.getWorkingOn().getResult().key());
                    factory.setWorkingOn(Township.noneKey);
                    if (factory.hasWaiting()) {
                        RecipeType.Recipe recipe = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipe.getKey());
                        factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                    }
                }
                context.update();
            });
        }
    }

    private ItemLore calculateLore(RecipeType.Recipe recipe, Barn barn) {
        List<Component> lore = new ArrayList<>();
        recipe.getIngredients().forEach((item, amount) -> lore.add(Utils.addResourceLine("<white>%s", barn.getItem(item.key()), amount, item.getName())));
        return ItemLore.lore(lore);
    }
}
