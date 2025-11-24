package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class TowncraftInventoryInteractEvent extends TowncraftInventoryEvent implements TowncraftCancellable {
    private Event.Result result = Event.Result.DEFAULT;

    public TowncraftInventoryInteractEvent(@NotNull TowncraftInventoryView view) {
        super(view);
    }

    public TowncraftPlayer getPlayer() {
        return this.getView().getPlayer();
    }

    public void setResult(@NotNull Result newResult) {
        this.result = newResult;
    }

    @Override
    public boolean isCancelled() {
        return this.getResult() == Result.DENY;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.setResult(cancel ? Result.DENY : Result.ALLOW);
    }
}
