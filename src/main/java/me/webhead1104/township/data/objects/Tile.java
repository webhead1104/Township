package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.WorldTileType;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class Tile {
    private WorldTileType tileType;
    private @Nullable Plot plot;
    private @Nullable Expansion expansion;
}
