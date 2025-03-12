package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.*;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@NoArgsConstructor
public class UserManager {
    public final Map<UUID, User> users = new HashMap<>();
    public final Map<UUID, Consumer<UUID>> playerCloseHandlers = new HashMap<>();
    public final Map<UUID, BukkitTask> menuTasks = new HashMap<>();

    public boolean hasPlayerCloseHandler(@NotNull UUID uuid) {
        Validate.notNull(uuid);
        return playerCloseHandlers.containsKey(uuid);
    }

    public @NotNull Consumer<UUID> getPlayerCloseHandler(@NotNull UUID uuid) {
        Validate.notNull(uuid);
        return Objects.requireNonNull(playerCloseHandlers.get(uuid));
    }

    public void addPlayerCloseHandler(@NotNull UUID uuid, @NotNull Consumer<UUID> handler) {
        Validate.notNull(uuid);
        Validate.notNull(handler);
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> playerCloseHandlers.put(uuid, handler), 1);
    }

    public void removePlayerCloseHandler(@NotNull UUID uuid) {
        Validate.notNull(uuid);
        playerCloseHandlers.remove(uuid);
    }

    public @NotNull User getUser(UUID uuid) {
        Optional<User> user = Optional.ofNullable(users.get(uuid));
        if (user.isEmpty()) {
            throw new NullPointerException("User not found!");
        } else return user.get();
    }

    public @NotNull Map<UUID, User> getUsers() {
        return users;
    }

    public void removeUser(UUID uuid) {
        if (userExists(uuid)) {
            users.remove(uuid);
        } else {
            throw new NullPointerException("User not found!");
        }
    }

    public boolean userExists(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid);
        return users.containsKey(uuid);
    }

    public void setUser(@NotNull UUID playerUUID, @NotNull User user) {
        Objects.requireNonNull(playerUUID, "The player's UUID must not be null");
        Objects.requireNonNull(user, "User must not be null");
        users.put(user.getUuid(), user);
    }

    public @NotNull User createUser(UUID uuid) {
        long start = System.currentTimeMillis();
        Barn barn = Barn.createItems();
        Animals animals = Animals.createAnimals();
        Factories factories = Factories.createFactories();
        World world = World.createWorld();
        Trains trains = new Trains();
        Township.logger.info("create done in {} mills", System.currentTimeMillis() - start);
        String name = Bukkit.getPlayer(uuid) == null ? "none" : Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() + "'s Town";
        return new User(uuid, name, new PlayerLevel(1, 0, uuid), 0, 0, 20, 27, barn, animals, factories, world, trains);
    }
}
