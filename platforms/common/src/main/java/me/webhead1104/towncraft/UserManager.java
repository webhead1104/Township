package me.webhead1104.towncraft;

import lombok.Getter;
import me.webhead1104.towncraft.data.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserManager {
    private final Map<UUID, User> users = new HashMap<>();

    public User getUser(UUID uuid) {
        if (userExists(uuid)) {
            return users.get(uuid);
        }
        throw new NullPointerException("User not found!");
    }

    public void removeUser(UUID uuid) {
        if (userExists(uuid)) {
            users.remove(uuid);
            return;
        }
        throw new NullPointerException("User not found!");

    }

    public boolean userExists(UUID uuid) {
        return users.containsKey(uuid);
    }

    public void setUser(UUID playerUUID, User user) {
        users.put(playerUUID, user);
    }
}
