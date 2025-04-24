package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.PlotType;

@Setter
@Getter
public class Plot {
    private int section;
    private int slot;
    private PlotType plotType;

    public Plot(int section, int slot, PlotType plotType) {
        this.section = section;
        this.slot = slot;
        this.plotType = plotType;
    }

    public static Plot fromJson(String json) {
        return Township.GSON.fromJson(json, Plot.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}
