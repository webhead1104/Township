package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.data.enums.WorldTileType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class Tile {
    private WorldTileType tileType;
    private @Nullable Plot plot;
    private @Nullable Expansion expansion;

    public @Nullable Plot getPlot() {
        return plot;
    }

    public void setPlot(@Nullable Plot plot) {
        this.plot = plot;
    }

    public @Nullable Expansion getExpansion() {
        return expansion;
    }

    public void setExpansion(@Nullable Expansion expansion) {
        this.expansion = expansion;
    }
}
