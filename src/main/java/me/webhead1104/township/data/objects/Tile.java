package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.WorldTileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class Tile {
    public static final @NotNull Codec<Tile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WorldTileType.CODEC.fieldOf("tileType").forGetter(Tile::getTileType),
            Plot.CODEC.optionalFieldOf("plot", null).forGetter(Tile::getPlot),
            Expansion.CODEC.optionalFieldOf("expansion", null).forGetter(Tile::getExpansion)
    ).apply(instance, Tile::new));
    private WorldTileType tileType;
    private @Nullable Plot plot;
    private @Nullable Expansion expansion;

    public Tile(WorldTileType tileType, @Nullable Plot plot, @Nullable Expansion expansion) {
        this.tileType = tileType;
        this.plot = plot;
        this.expansion = expansion;
    }
}
