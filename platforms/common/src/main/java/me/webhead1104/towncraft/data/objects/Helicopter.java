/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
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
package me.webhead1104.towncraft.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.exceptions.TowncraftException;
import me.webhead1104.towncraft.utils.Utils;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@ConfigSerializable
public final class Helicopter {
    private static final Random random = new Random();
    private static final Duration DUMP_COOLDOWN = Duration.ofMinutes(30);
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private final List<PendingOrder> pendingOrders = new ArrayList<>();

    public void completeOrder(User user, int slot) {

        if (!hasOrder(slot)) {
            return;
        }

        Order order = getOrder(slot);

        if (!order.canFulFIll(user.getBarn())) {
            return;
        }

        for (ItemAndAmount item : order.getItems()) {
            user.getBarn().removeAmountFromItem(item.getItemKey(), item.getAmount());
        }

        user.setCoins(user.getCoins() + order.getCoins());
        user.setXp(user.getXp() + order.getXp());

        removeOrder(slot);

        fillHelicopterOrders(user.getLevel(), user.getPopulation());
    }

    public void dumpOrder(int slot) {
        if (!hasOrder(slot)) {
            return;
        }

        removeOrder(slot);

        PendingOrder pending = new PendingOrder();
        pending.setReadyAt(Instant.now().plus(DUMP_COOLDOWN));
        pendingOrders.add(pending);
    }

    private int getMaxOrdersForPopulation(int population) {
        if (population < 200) return 3;
        if (population < 500) return 4;
        if (population < 1000) return 5;
        if (population < 2000) return 6;
        if (population < 4000) return 7;
        if (population < 8000) return 8;
        return 9;
    }

    public Order getOrder(int slot) {
        return orders.get(slot);
    }

    public boolean hasOrder(int slot) {
        return orders.containsKey(slot);
    }

    public void removeOrder(int slot) {
        orders.remove(slot);
    }

    public void fillHelicopterOrders(int level, int population) {
        int maxOrders = getMaxOrdersForPopulation(population);

        placeReadyPendingOrders(level, maxOrders);

        int reservedByCooldown = countNotReadyPendingOrders();
        int effectiveCurrent = orders.size() + reservedByCooldown;

        int ordersToAdd = maxOrders - effectiveCurrent;
        if (ordersToAdd <= 0) {
            return;
        }

        List<Integer> emptySlots = new ArrayList<>();
        for (int slot = 0; slot < 27; slot++) {
            if (!hasOrder(slot)) {
                emptySlots.add(slot);
            }
        }

        if (emptySlots.isEmpty()) {
            return;
        }

        Collections.shuffle(emptySlots, random);

        int toFill = Math.min(ordersToAdd, emptySlots.size());
        for (int i = 0; i < toFill; i++) {
            int slot = emptySlots.get(i);
            orders.put(slot, generateOrder(level));
        }
    }

    private int countNotReadyPendingOrders() {
        if (pendingOrders.isEmpty()) {
            return 0;
        }
        Instant now = Instant.now();
        int count = 0;

        for (PendingOrder pending : pendingOrders) {
            Instant readyAt = pending.getReadyAt();
            if (readyAt != null && !Instant.EPOCH.equals(readyAt) && readyAt.isAfter(now)) {
                count++;
            }
        }
        return count;
    }

    private void placeReadyPendingOrders(int level, int maxOrders) {
        if (pendingOrders.isEmpty()) {
            return;
        }

        Instant now = Instant.now();

        List<Integer> emptySlots = new ArrayList<>();
        for (int slot = 0; slot < 27; slot++) {
            if (!hasOrder(slot)) {
                emptySlots.add(slot);
            }
        }

        if (emptySlots.isEmpty()) {
            return;
        }

        Collections.shuffle(emptySlots, random);

        Iterator<PendingOrder> it = pendingOrders.iterator();
        int emptyIndex = 0;

        while (it.hasNext()) {
            if (orders.size() >= maxOrders) {
                return;
            }
            if (emptyIndex >= emptySlots.size()) {
                return;
            }

            PendingOrder pending = it.next();
            Instant readyAt = pending.getReadyAt();

            if (readyAt != null && readyAt.isAfter(now)) {
                continue;
            }

            int targetSlot = emptySlots.get(emptyIndex++);
            orders.put(targetSlot, generateOrder(level));
            it.remove();
        }
    }

    public Order generateOrder(int playerLevel) {
        Order order = new Order();

        List<ItemType.Item> availableItems = Towncraft.getDataLoader(ItemType.class).values().stream()
                .filter(item -> item.getLevelNeeded() <= playerLevel)
                .filter(item -> item.getSellPrice() > 0)
                .filter(item -> item.getBaseWeight() > 0)
                .filter(item -> !ItemType.CONSTRUCTION_MATERIALS.contains(item.key()))
                .collect(Collectors.toList());

        if (availableItems.isEmpty()) {
            throw new TowncraftException("No items found for level " + playerLevel);
        }

        int itemTypeCount = 1 + random.nextInt(Math.min(4, playerLevel / 5 + 1));
        itemTypeCount = Math.min(itemTypeCount, availableItems.size());

        int totalValue = 0;
        List<ItemType.Item> selectedItems = new ArrayList<>();

        for (int i = 0; i < itemTypeCount; i++) {
            ItemType.Item item = Utils.selectWeightedItem(availableItems, selectedItems, playerLevel);
            if (item == null) break;

            selectedItems.add(item);

            int quantity = 1 + random.nextInt(Math.min(6, playerLevel / 3 + 2));

            ItemAndAmount itemAndAmount = new ItemAndAmount(item.key(), quantity, item);
            order.getItems().add(itemAndAmount);

            totalValue += item.getSellPrice() * quantity;
        }

        double coinMultiplier = 0.8 + (random.nextDouble() * 0.4); // 80-120%
        order.setCoins((int) (totalValue * coinMultiplier));

        double xpMultiplier = 0.1 + (random.nextDouble() * 0.2); // 10-30%
        order.setXp((int) (order.getCoins() * xpMultiplier));

        return order;
    }

    @Getter
    @Setter
    @ConfigSerializable
    public static final class PendingOrder {
        private Instant readyAt = Instant.EPOCH;
    }

    @Getter
    @Setter
    @ConfigSerializable
    public static final class Order {
        private final List<ItemAndAmount> items = new ArrayList<>();
        private int xp;
        private int coins;

        public boolean canFulFIll(Barn barn) {
            for (ItemAndAmount item : items) {
                if (barn.getItem(item.getItemKey()) < item.getAmount()) {
                    return false;
                }
            }
            return true;
        }
    }
}