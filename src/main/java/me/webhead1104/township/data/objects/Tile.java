package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.enums.WorldTileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Getter
@Setter
public class Tile {
    public static final @NotNull Codec<Tile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WorldTileType.CODEC.fieldOf("tileType").forGetter(Tile::getTileType),
            Plot.CODEC.lenientOptionalFieldOf("plot").xmap(
                    o -> o.orElse(new Plot(-1, -1, PlotType.NONE)),
                    o -> Optional.of(o != null && o.getSection() != -1 && o.getSlot() != -1 ? o : new Plot(-1, -1, PlotType.NONE))
            ).forGetter(Tile::getPlot),
            Expansion.CODEC.lenientOptionalFieldOf("expansion").xmap(
                    o -> o.orElse(new Expansion(-1, -1, 0, 0)),
                    o -> Optional.of(o != null && o.getSection() != -1 && o.getSlot() != -1 ? o : new Expansion(-1, -1, 0, 0))
            ).forGetter(Tile::getExpansion)
    ).apply(instance, Tile::new));
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
