package me.webhead1104.towncraft.tiles;

import me.devnatan.inventoryframework.context.Context;
import me.webhead1104.towncraft.data.objects.WorldSection;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface TimeFinishable {

    @Nullable Instant getInstant();

    void setInstant(@Nullable Instant instant);

    void onFinish(Context context, WorldSection worldSection, int slot);

    default boolean isFinished() {
        Instant finishAt = getInstant();
        return finishAt != null && Instant.now().isAfter(finishAt.minusSeconds(1));
    }

    default void handleUpdate(Context slotContext, WorldSection worldSection, int slot) {
        if (getInstant() == null || !isFinished()) {
            return;
        }
        setInstant(null);
        onFinish(slotContext, worldSection, slot);
    }
}
