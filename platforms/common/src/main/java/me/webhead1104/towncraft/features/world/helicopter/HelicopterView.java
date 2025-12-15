package me.webhead1104.towncraft.features.world.helicopter;

import lombok.extern.slf4j.Slf4j;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.data.objects.Barn;
import me.webhead1104.towncraft.data.objects.Helicopter;
import me.webhead1104.towncraft.data.objects.ItemAndAmount;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HelicopterView extends TowncraftView {
    private final State<Helicopter> helicopterState = computedState(context -> userState.get(context).getHelicopter());

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title(Msg.format("Helicopter"));
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        context.slot(49).updateOnClick().onRender(indicatorContext -> {
            Helicopter helicopter = helicopterState.get(indicatorContext);

            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.GRAY_STAINED_GLASS_PANE);
            itemStack.setName(Msg.format("Dump cooldown"));

            List<Component> lore = new ArrayList<>();
            Optional<Instant> nextReadyAt = getNextPendingReadyAt(helicopter);

            if (nextReadyAt.isEmpty()) {
                lore.add(Msg.format("<green>No pending orders"));
            } else {
                Instant now = Instant.now();
                Instant readyAt = nextReadyAt.get();
                if (!readyAt.isAfter(now)) {
                    lore.add(Msg.format("<green>Ready now"));
                } else {
                    lore.add(Msg.format("<red>Next order in: %s", Utils.format(now, readyAt)));
                }
            }

            itemStack.setLore(lore);
            indicatorContext.setItem(itemStack);
        });

        for (int i = 0; i < 27; i++) {
            int slot = i;
            context.availableSlot().updateOnClick().onRender(slotRenderContext -> {
                TowncraftItemStack itemStack;
                List<Component> lore = new ArrayList<>();
                Helicopter helicopter = helicopterState.get(slotRenderContext);

                helicopter.fillHelicopterOrders(
                        userState.get(slotRenderContext).getLevel(),
                        userState.get(slotRenderContext).getPopulation()
                );

                if (helicopter.hasOrder(slot)) {
                    Helicopter.Order order = helicopter.getOrder(slot);
                    Barn barn = userState.get(slotRenderContext).getBarn();

                    if (order.canFulFIll(barn)) {
                        itemStack = TowncraftItemStack.of(TowncraftMaterial.GREEN_STAINED_GLASS_PANE);
                        itemStack.setName(Msg.format("<green>Order Ready"));
                    } else {
                        itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_STAINED_GLASS_PANE);
                        itemStack.setName(Msg.format("<red>Can't Fulfill Order"));
                    }

                    for (ItemAndAmount item : order.getItems()) {
                        lore.add(Utils.addResourceLine("<white>" + item.getItem().getName(), barn.getItem(item.getItemKey()), item.getAmount()));
                    }

                    lore.add(Component.empty());
                    lore.add(Msg.format("<aqua>Xp<white>: %d", order.getXp()));
                    lore.add(Msg.format("<gold>Coins<white>: %d", order.getCoins()));
                    lore.add(Component.empty());
                    lore.add(Msg.format("<green>Press <key:key.attack> to send the order"));
                    lore.add(Msg.format("<aqua>Or press <key:key.use> to dump the order"));
                } else {
                    itemStack = TowncraftItemStack.of(TowncraftMaterial.GRAY_STAINED_GLASS_PANE);
                    itemStack.setName(Msg.format("Waiting for Order"));
                }

                itemStack.setLore(lore);
                slotRenderContext.setItem(itemStack);
            }).onClick(slotClickContext -> {
                Helicopter helicopter = helicopterState.get(slotClickContext);
                if (!helicopter.hasOrder(slot)) {
                    return;
                }
                if (slotClickContext.isRightClick()) {
                    helicopter.dumpOrder(slot);
                    return;
                }
                Helicopter.Order order = helicopter.getOrder(slot);
                Barn barn = userState.get(slotClickContext).getBarn();
                if (order.canFulFIll(barn)) {
                    helicopter.completeOrder(userState.get(slotClickContext), slot);
                }
            });
        }
    }

    private Optional<Instant> getNextPendingReadyAt(Helicopter helicopter) {
        return helicopter.getPendingOrders().stream()
                .map(Helicopter.PendingOrder::getReadyAt)
                .filter(instant -> instant != null && !Instant.EPOCH.equals(instant))
                .min(Comparator.naturalOrder());
    }
}