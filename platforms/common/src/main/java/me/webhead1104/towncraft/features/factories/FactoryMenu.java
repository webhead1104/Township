/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.features.factories;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.Barn;
import me.webhead1104.towncraft.data.objects.Factories;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FactoryMenu extends TowncraftView {
    private final State<Key> keyState = initialState();
    private final State<FactoryType.Factory> factoryState = computedState(context -> Towncraft.getDataLoader(FactoryType.class).get(keyState.get(context)));

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
    public void onFirstRender(@NotNull RenderContext context) {
        User user = userState.get(context);
        FactoryType.Factory factoryData = this.factoryState.get(context);
        Factories.Factory factory = user.getFactories().getFactory(factoryData.key());

        int recipeSlot = 45;
        for (RecipeType.Recipe recipe : factoryData.getRecipes()) {
            context.slot(recipeSlot++).onRender(slotRenderContext -> {
                TowncraftItemStack stack = recipe.getResult().getItemStack();
                stack.setLore(calculateLore(recipe, user.getBarn()));
                slotRenderContext.setItem(stack);
            }).updateOnClick().onClick(slotClickContext -> {
                if (!factory.canAddWaitingOrWorkingOn() || !recipe.hasRequiredItems(user.getBarn())) return;
                recipe.getIngredients().forEach((item, value) -> user.getBarn().removeAmountFromItem(item.key(), value));
                if (factory.canSetWorkingOn()) {
                    factory.setWorkingOn(recipe.key());
                    factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                } else if (factory.canAddWaiting()) {
                    factory.addWaiting(recipe.key());
                }
                context.update();
            });
        }

        int waitingSlot = 29;
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            context.slot(waitingSlot++).onRender(slotRenderContext -> {
                if (factory.getWaiting(finalI).equals(Towncraft.NONE_KEY)) {
                    TowncraftItemStack stack = TowncraftItemStack.of(TowncraftMaterial.HOPPER);
                    stack.setName(Msg.format("<red>Nothing is being made right now"));
                    stack.setLore(Msg.format("<grey>Maybe you should make something!"));
                    slotRenderContext.setItem(stack);
                    return;
                }
                TowncraftItemStack stack = factory.getWaiting(finalI).getResult().getItemStack();
                stack.setLore(Msg.format("<dark_green>Waiting..."));
                slotRenderContext.setItem(stack);
            });
        }

        context.slot(27).onRender(slotRenderContext -> {
            if (!factory.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(factory.getInstant().minusSeconds(1))) {
                factory.setInstant(Instant.EPOCH);
                if (factory.canAddCompleted()) {
                    factory.addCompleted(factory.getWorkingOn().getResult().key());
                    factory.setWorkingOn(Towncraft.NONE_KEY);
                    if (factory.hasWaiting()) {
                        RecipeType.Recipe recipe = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipe.key());
                        factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                    }
                }
                context.update();
            }
            if (factory.getWorkingOn().equals(Towncraft.NONE_KEY)) {
                TowncraftItemStack stack = TowncraftItemStack.of(TowncraftMaterial.RED_CANDLE);
                stack.setName(Msg.format("<red>Nothing is being made right now"));
                stack.setLore(Msg.format("<grey>Maybe you should make something!"));
                slotRenderContext.setItem(stack);
                return;
            }
            if (factory.getInstant().equals(Instant.EPOCH)) {
                TowncraftItemStack stack = factory.getWorkingOn().getResult().getItemStack();
                stack.setLore(Msg.format("<red>Your production queue is full!"));
                slotRenderContext.setItem(stack);
                return;
            }
            TowncraftItemStack stack = factory.getWorkingOn().getResult().getItemStack();
            stack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), factory.getInstant())));
            slotRenderContext.setItem(stack);
        });

        int completedSlot = 12;
        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            context.slot(completedSlot++).onRender(slotRenderContext -> {
                RecipeType.Recipe recipe = factory.getCompleted(finalI);
                if (recipe.equals(Towncraft.NONE_KEY)) {
                    TowncraftItemStack stack = TowncraftItemStack.of(TowncraftMaterial.CHEST);
                    stack.setName(Msg.format("<red>Nothing is being made right now"));
                    stack.setLore(Msg.format("<grey>Maybe you should make something!"));
                    slotRenderContext.setItem(stack);
                    return;
                }
                TowncraftItemStack stack = recipe.getResult().getItemStack();
                stack.setLore(Msg.format("<green>Click to claim!"));
                slotRenderContext.setItem(stack);
            }).onClick(slotClickContext -> {
                if (factory.getCompleted(finalI).equals(Towncraft.NONE_KEY)) return;
                user.getBarn().addAmountToItem(factory.getCompleted(finalI).key(), 1);
                user.addXp(factory.getCompleted(finalI).getXpGiven());
                factory.setCompleted(finalI, Towncraft.NONE_KEY);
                if (factory.getInstant().equals(Instant.EPOCH) && !factory.getWorkingOn().equals(Towncraft.NONE_KEY)) {
                    factory.addCompleted(factory.getWorkingOn().getResult().key());
                    factory.setWorkingOn(Towncraft.NONE_KEY);
                    if (factory.hasWaiting()) {
                        RecipeType.Recipe recipe = factory.removeFirstWaiting();
                        factory.setWorkingOn(recipe.key());
                        factory.setInstant(Instant.now().plusSeconds(recipe.getTime().getSeconds()));
                    }
                }
                context.update();
            });
        }
    }

    private List<Component> calculateLore(RecipeType.Recipe recipe, Barn barn) {
        List<Component> lore = new ArrayList<>();
        recipe.getIngredients().forEach((item, amount) -> lore.add(Utils.addResourceLine("<white>%s", barn.getItem(item.key()), amount, item.getName())));
        return lore;
    }
}
