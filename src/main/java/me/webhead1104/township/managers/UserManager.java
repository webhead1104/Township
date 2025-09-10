package me.webhead1104.township.managers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.dataVersions.DataVersion;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.*;

@NoArgsConstructor
@Getter
public class UserManager {
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<Integer, DataVersion> dataVersions = new HashMap<>();

    public void loadDataVersions() {
        ClassGraph graph = new ClassGraph().acceptPackages("me.webhead1104.township.dataVersions").enableAllInfo();

        try (ScanResult result = graph.scan()) {
            result.getClassesImplementing(DataVersion.class).loadClasses().forEach(clazz -> {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    DataVersion dataVersion = (DataVersion) constructor.newInstance();
                    dataVersions.put(dataVersion.getVersion(), dataVersion);
                } catch (Exception e) {
                    Township.logger.error("An error occurred whilst loading data versions!", e);
                }
            });
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst loading data versions!", e);
        }
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
}
