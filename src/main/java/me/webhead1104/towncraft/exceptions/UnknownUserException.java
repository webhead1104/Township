package me.webhead1104.towncraft.exceptions;

import java.util.UUID;

public class UnknownUserException extends TowncraftException {

    public UnknownUserException(UUID userUUID) {
        super("Unknown user" + userUUID);
    }
}
