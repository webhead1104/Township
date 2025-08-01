package me.webhead1104.township.menus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
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
    private final MutableState<AnimalType> animalType = initialState();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        AnimalType animalType = this.animalType.get(context);
        context.modifyConfig().title(animalType.getMenuTitle());
        if (!Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getAnimals().getAnimalBuilding(animalType).isUnlocked())
            context.setCancelled(true);
        Player player = context.getPlayer();
        player.getInventory().clear();
        player.setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onClose(@NotNull CloseContext context) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> Township.getWorldManager().openWorldMenu(context.getPlayer()), 1);
    }

    @Override
    public void onFirstRender(RenderContext render) {
        Player player = render.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Animals animals = user.getAnimals();
        AnimalType animalType = this.animalType.get(render);
        int slot = 11;
        for (int i = 0; i < 6; ++i) {
            Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType).getAnimal(i);

            render.slot(slot).onRender(context -> {
                if (!animal.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(animal.getInstant().minusSeconds(1))) {
                    animal.setFeed(false);
                    animal.setProduct(true);
                    animal.setInstant(Instant.EPOCH);
                    context.update();
                }
                ItemStack stack = animalType.getAnimalItemStack();
                if (!animal.getInstant().equals(Instant.EPOCH)) {
                    stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), animal.getInstant())))));
                }
                context.setItem(stack);
            });

            render.slot(slot + 9).onRender(context -> {
                if (!animal.isProduct()) {
                    context.setItem(ItemStack.empty());
                    return;
                }
                context.setItem(animalType.getProductType().getItemStack());
            }).onClick(context -> {
                user.getBarn().addAmountToItem(animalType.getProductType(), 1);
                animal.setProduct(false);
                context.update();
            });
            slot++;
        }

        render.slot(36).onRender(context -> {
            ItemStack stack = animalType.getFeedType().getItemStack();
            stack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<white>%s", user.getBarn().getItem(animalType.getFeedType())))));
            context.setItem(stack);
        }).onClick(context -> {
            if (user.getBarn().getItem(animalType.getFeedType()) >= 1) {
                for (int i = 0; i < 6; ++i) {
                    Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType).getAnimal(i);
                    if (!animal.isFeed() && !animal.isProduct() && user.getBarn().getItem(animalType.getFeedType()) >= 1) {
                        animal.setFeed(true);
                        animal.setInstant(Instant.now().plusSeconds(animalType.getTimeTakesToFeed().getSeconds()));
                        user.getBarn().removeAmountFromItem(animalType.getFeedType(), 1);
                    }
                }
                context.update();
            }
        });
    }
}
