package me.webhead1104.towncraft;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataVersions.DataVersion;
import me.webhead1104.towncraft.utils.ClassGraphUtils;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.*;

@NoArgsConstructor
@Getter
public class UserManager {
    private final Map<UUID, User> users = new HashMap<>();
    private final List<DataVersion> dataVersions = new ArrayList<>();
    private final ConfigurationTransformation.VersionedBuilder versionedBuilder = ConfigurationTransformation.versionedBuilder();

    public void loadDataVersions() {
        dataVersions.addAll(ClassGraphUtils.getImplementedClasses(DataVersion.class, "me.webhead1104.towncraft.dataVersions"));
        versionedBuilder.versionKey("version");
        for (DataVersion dataVersion : dataVersions) {
            versionedBuilder.addVersion(dataVersion.getVersion(), dataVersion.getTransformation());
        }
    }

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
