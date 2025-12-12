package me.webhead1104.towncraft.features.animals;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.Animals;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class AnimalMenu extends TowncraftView {
    private final State<Key> keyState = initialState();
    private final State<AnimalType.Animal> animalState = computedState(context -> Towncraft.getDataLoader(AnimalType.class).get(keyState.get(context)));

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        AnimalType.Animal animal = animalState.get(context);
        context.modifyConfig().title(animal.getMenuTitle());
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        User user = userState.get(context);
        Animals animals = user.getAnimals();
        AnimalType.Animal animalType = animalState.get(context);
        int slot = 11;
        for (int i = 0; i < 6; ++i) {
            Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType.key()).getAnimal(i);
            context.slot(slot).onUpdate(slotContext -> {
                if (!animal.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(animal.getInstant().minusSeconds(1))) {
                    animal.setFeed(false);
                    animal.setProduct(true);
                    animal.setInstant(Instant.EPOCH);
                    slotContext.update();
                }
            }).onRender(slotRenderContext -> {
                TowncraftItemStack stack = animalType.getAnimalItemStack();
                if (!animal.getInstant().equals(Instant.EPOCH)) {
                    stack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), animal.getInstant())));
                }
                slotRenderContext.setItem(stack);
            });

            context.slot(slot + 9).withItem(animalType.getProduct().getItemStack())
                    .displayIf(animal::isProduct).updateOnClick().onClick(slotClickContext -> {
                        user.getBarn().addAmountToItem(animalType.getProduct().key(), 1);
                        user.addXp(animalType.getClaimXp());
                        animal.setProduct(false);
                    });
            slot++;
        }

        context.slot(36).updateOnClick().onRender(slotRenderContext -> {
            TowncraftItemStack stack = animalType.getFeed().getItemStack();
            stack.setLore(Msg.format("<white>%s", user.getBarn().getItem(animalType.getFeedKey())));
            slotRenderContext.setItem(stack);
        }).onClick(slotClickContext -> {
            if (user.getBarn().getItem(animalType.getFeedKey()) >= 1) {
                for (int i = 0; i < 6; ++i) {
                    Animals.AnimalBuilding.Animal animal = animals.getAnimalBuilding(animalType.key()).getAnimal(i);
                    if (!animal.isFeed() && !animal.isProduct() && user.getBarn().getItem(animalType.getFeedKey()) >= 1) {
                        animal.setFeed(true);
                        animal.setInstant(Instant.now().plusSeconds(animalType.getTime().getSeconds()));
                        user.getBarn().removeAmountFromItem(animalType.getFeedKey(), 1);
                    }
                }
            }
        });
    }
}
