package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.enums.WorldTileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class Tile {
    private WorldTileType tileType;
    private @NotNull Plot plot;
    private @NotNull Expansion expansion;

    public Tile(WorldTileType tileType, @Nullable Plot plot, @Nullable Expansion expansion) {
        this.tileType = tileType;
        this.plot = plot == null ? new Plot(-1, -1, PlotType.NONE) : plot;
        this.expansion = expansion == null ? new Expansion(-1, -1, 0, 0) : expansion;
    }

    public @Nullable Plot getPlot() {
        return plot.getSection() == -1 && plot.getSlot() == -1 ? null : plot;
    }

    public void setPlot(@Nullable Plot plot) {
        this.plot = plot == null ? new Plot(-1, -1, PlotType.NONE) : plot;
    }

    public @Nullable Expansion getExpansion() {
        return expansion.getSection() == -1 && expansion.getSlot() == -1 ? null : expansion;
    }

    public void setExpansion(@Nullable Expansion expansion) {
        this.expansion = expansion == null ? new Expansion(-1, -1, 0, 0) : expansion;
    }
}
