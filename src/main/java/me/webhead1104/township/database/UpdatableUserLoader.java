package me.webhead1104.township.database;

import lombok.Getter;

import java.io.IOException;

public abstract class UpdatableUserLoader implements UserLoader {

    public abstract void update() throws NewerStorageException, IOException;

    @Getter
    public static class NewerStorageException extends Exception {
        private final int implementationVersion;
        private final int storageVersion;

        public NewerStorageException(int implementationVersion, int storageVersion) {
            this.implementationVersion = implementationVersion;
            this.storageVersion = storageVersion;
        }
    }
}
