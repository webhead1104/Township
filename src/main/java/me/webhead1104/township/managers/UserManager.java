package me.webhead1104.township.managers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.dataVersions.DataVersion;
import me.webhead1104.township.utils.ClassGraphUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@NoArgsConstructor
@Getter
public class UserManager {
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<Integer, DataVersion> dataVersions = new HashMap<>();

    public void loadDataVersions() {
        for (DataVersion implementedClass : ClassGraphUtils.getImplementedClasses(DataVersion.class, "me.webhead1104.township.dataVersions")) {
            dataVersions.put(implementedClass.getVersion(), implementedClass);
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
