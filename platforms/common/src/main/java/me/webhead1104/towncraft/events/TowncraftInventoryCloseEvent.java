package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import org.jetbrains.annotations.NotNull;

@Getter
public class TowncraftInventoryCloseEvent extends TowncraftInventoryEvent {
    private final Reason reason;

    public TowncraftInventoryCloseEvent(@NotNull TowncraftInventoryView view) {
        this(view, Reason.UNKNOWN);
    }

    public TowncraftInventoryCloseEvent(TowncraftInventoryView view, Reason reason) {
        super(view);
        this.reason = reason;
    }

    public enum Reason {
        UNKNOWN,
        CANT_USE,
        UNLOADED,
        OPEN_NEW,
        PLAYER,
        DISCONNECT,
        DEATH,
        PLUGIN,
    }
}
