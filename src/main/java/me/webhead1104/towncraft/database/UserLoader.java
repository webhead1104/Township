package me.webhead1104.towncraft.database;

import me.webhead1104.towncraft.exceptions.UnknownUserException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserLoader {

    String readUser(UUID userUUID) throws UnknownUserException, IOException;

    boolean userExists(UUID userUUID) throws IOException;

    List<UUID> listUsers() throws IOException;

    void saveUser(UUID userUUID, String userData) throws IOException;

    void deleteUser(UUID userUUID) throws UnknownUserException, IOException;
}
