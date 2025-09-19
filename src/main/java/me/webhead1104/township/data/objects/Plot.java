package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.features.world.plots.PlotType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;

@Setter
@Getter
@ConfigSerializable
@NoArgsConstructor
public class Plot {
    private int section;
    private int slot;
    private PlotType plotType;
    private Instant instant = Instant.EPOCH;
    private boolean claimable = false;

    public Plot(int section, int slot, PlotType plotType) {
        this.section = section;
        this.slot = slot;
        this.plotType = plotType;
    }
}
