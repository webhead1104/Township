package me.webhead1104.towncraft.database;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.exceptions.UnknownUserException;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record FileLoader(File userDir) implements UserLoader {
    private static final FilenameFilter USER_FILE_FILTER = ((dir, name) -> name.endsWith(".json"));

    public FileLoader(File userDir) {
        this.userDir = userDir;

        if (userDir.exists() && !userDir.isDirectory()) {
            Towncraft.logger.warn("A file named '{}' has been deleted, as this is the name used for the user data directory.", userDir.getName());
            if (!userDir.delete())
                throw new IllegalStateException("Failed to delete the file named '" + userDir.getName() + "'.");
        }

        if (!userDir.exists() && !userDir.mkdirs())
            throw new IllegalStateException("Failed to create the user data directory.");
    }

    @Override
    public String readUser(UUID userUUID) throws UnknownUserException, IOException {
        if (!userExists(userUUID)) {
            throw new UnknownUserException(userUUID);
        }
        try (FileInputStream fis = new FileInputStream(new File(userDir, userUUID + ".json"))) {
            return new String(fis.readAllBytes());
        }
    }

    @Override
    public boolean userExists(UUID userUUID) {
        return new File(userDir, userUUID + ".json").exists();
    }

    @Override
    public List<UUID> listUsers() throws IOException {
        String[] users = userDir.list(USER_FILE_FILTER);

        if (users == null) {
            throw new NotDirectoryException(userDir.getPath());
        }

        return Arrays.stream(users).map((c) -> c.substring(0, c.length() - 5)).map(UUID::fromString).collect(Collectors.toList());
    }

    @Override
    public void saveUser(UUID userUUID, String userData) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(userDir, userUUID + ".json"))) {
            fos.write(userData.getBytes());
        }
    }

    @Override
    public void deleteUser(UUID userUUID) throws UnknownUserException, IOException {
        if (!userExists(userUUID)) {
            throw new UnknownUserException(userUUID);
        } else {
            if (!new File(userDir, userUUID + ".json").delete()) {
                throw new IOException("Failed to delete the user file. File#delete() returned false.");
            }
        }
    }
}
