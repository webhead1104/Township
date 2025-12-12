package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class TowncraftInventoryEvent extends Event {
    protected TowncraftInventoryView view;

    public TowncraftInventoryEvent(@NotNull TowncraftInventoryView view) {
        this.view = view;
    }

    public TowncraftInventory getInventory() {
        return view.getTopInventory();
    }

    public List<? extends TowncraftPlayer> getViewers() {
        return view.getTopInventory().getViewers();
    }
}
