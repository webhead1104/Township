package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.objects.Animals;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

public class AnimalMenu extends View {
    private final MutableState<AnimalType> animalTypeState = initialState();
    private final State<User> userState = computedState(context -> Township.getUserManager().getUser(context.getPlayer().getUniqueId()));

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        AnimalType animalType = animalTypeState.get(context);
        context.modifyConfig().title(animalType.getMenuTitle());
        Player player = context.getPlayer();
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTask(Township.getInstance(), () -> Township.getWorldManager().openWorldMenu(context.getPlayer()));
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        User user = userState.get(context);
        Animals animals = user.getAnimals();
        AnimalType animalType = animalTypeState.get(context);
        int slot = 11;
        for (int i = 0; i < 6; ++i) {
            Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType).getAnimal(i);
            context.slot(slot).onRender(slotRenderContext -> {
                if (!animal.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(animal.getInstant().minusSeconds(1))) {
                    animal.setFeed(false);
                    animal.setProduct(true);
                    animal.setInstant(Instant.EPOCH);
                    slotRenderContext.update();
                }
                ItemStack stack = animalType.getAnimalItemStack();
                if (!animal.getInstant().equals(Instant.EPOCH)) {
                    stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), animal.getInstant())))));
                }
                slotRenderContext.setItem(stack);
            });

            context.slot(slot + 9).onRender(slotRenderContext -> {
                if (!animal.isProduct()) {
                    slotRenderContext.setItem(ItemStack.empty());
                    return;
                }
                slotRenderContext.setItem(animalType.getProductType().getItemStack());
            }).onClick(slotClickContext -> {
                user.getBarn().addAmountToItem(animalType.getProductType(), 1);
                animal.setProduct(false);
                slotClickContext.update();
            });
            slot++;
        }

        context.slot(36).onRender(slotRenderContext -> {
            ItemStack stack = animalType.getFeedType().getItemStack();
            stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<white>%s", user.getBarn().getItem(animalType.getFeedType())))));
            slotRenderContext.setItem(stack);
        }).onClick(slotClickContext -> {
            if (user.getBarn().getItem(animalType.getFeedType()) >= 1) {
                for (int i = 0; i < 6; ++i) {
                    Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType).getAnimal(i);
                    if (!animal.isFeed() && !animal.isProduct() && user.getBarn().getItem(animalType.getFeedType()) >= 1) {
                        animal.setFeed(true);
                        animal.setInstant(Instant.now().plusSeconds(animalType.getTimeTakesToFeed().getSeconds()));
                        user.getBarn().removeAmountFromItem(animalType.getFeedType(), 1);
                    }
                }
                slotClickContext.update();
            }
        });
    }
}
