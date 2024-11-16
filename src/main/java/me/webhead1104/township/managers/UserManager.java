package me.webhead1104.township.managers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@NoArgsConstructor
public class UserManager {
    private final Map<UUID, User> users = new HashMap<>();

    public @NotNull User getUser(UUID uuid) {
        Optional<User> user = Optional.ofNullable(users.get(uuid));
        if (user.isEmpty()) {
            throw new NullPointerException("User not found!");
        } else return user.get();
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
        Trains trains = Trains.createTrains();
        Township.logger.info(STR."create done in \{System.currentTimeMillis() - start} mills");
        return new User(uuid, "none", new PlayerLevel(1, 0, uuid), 0, 0, 20, 27, barn, animals, factories, world, trains);
    }
}
