package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.plots.PlotMenu;
import me.webhead1104.towncraft.features.world.plots.PlotType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlotTile extends BuildingTile implements TimeFinishable {
    private PlotType plotType = PlotType.NONE;
    private @Nullable Instant instant;
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
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        if (plotType.equals(PlotType.NONE) || instant == null) {
            return plotType.getMenuItem();
        }

        TowncraftItemStack itemStack = plotType.getMenuItem();
        itemStack.setLore(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), instant)));
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
            context.getUser().getBarn().addAmountToItem(plotType.getItem().key(), 1);
            this.plotType = PlotType.NONE;
        }
        return false;
    }

    @Override
    public void onFinish(Context context, WorldSection worldSection, int slot) {
        this.claimable = true;
    }
}
