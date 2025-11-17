package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.plots.PlotMenu;
import me.webhead1104.towncraft.features.world.plots.PlotType;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlotTile extends BuildingTile implements TimeFinishable {
    private PlotType plotType = PlotType.NONE;
    @Nullable
    private Instant instant;
    private boolean claimable = false;

    @Keep
    public PlotTile(@NonNull PlotType plotType, @Nullable Instant instant, boolean claimable) {
        super(Towncraft.key("plot"));
        this.plotType = plotType;
        this.instant = instant;
        this.claimable = claimable;
    }

    @Keep
    public PlotTile() {
        super(Towncraft.key("plot"));
    }

    @Override
    public ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (plotType.equals(PlotType.NONE) || instant == null) {
            return plotType.getMenuItem();
        }

        ItemStack itemStack = plotType.getMenuItem();
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), instant)))));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        if (plotType.equals(PlotType.NONE)) {
            Map<String, Object> map = new HashMap<>(Map.of(
                    "slot", slot,
                    "section", worldSection.getSection()
            ));
            context.openForPlayer(PlotMenu.class, map);
            return true;
        } else if (claimable) {
            this.claimable = false;
            Towncraft.getUserManager().getUser(context.getPlayer().getUniqueId()).getBarn().addAmountToItem(plotType.getItem().key(), 1);
            this.plotType = PlotType.NONE;
        }
        return false;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        this.claimable = true;
    }
}
