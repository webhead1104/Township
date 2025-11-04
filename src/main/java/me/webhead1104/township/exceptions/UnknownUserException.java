package me.webhead1104.township.exceptions;

import java.util.UUID;

public class UnknownUserException extends TownshipException {

    public UnknownUserException(UUID userUUID) {
        super("Unknown user" + userUUID);
    }
}
