package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.data.enums.PlotType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Setter
@Getter
@ConfigSerializable
@NoArgsConstructor
public class Plot {
    public static final int LATEST_VERSION = 1;
    private int version = LATEST_VERSION;
    private int section;
    private int slot;
    private PlotType plotType;

    public Plot(int section, int slot, PlotType plotType) {
        this.section = section;
        this.slot = slot;
        this.plotType = plotType;
    }
}
