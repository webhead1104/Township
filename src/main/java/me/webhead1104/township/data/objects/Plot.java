package me.webhead1104.township.data.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.PlotType;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class Plot {
    public static final @NotNull Codec<Plot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("section").forGetter(Plot::getSection),
            Codec.INT.fieldOf("slot").forGetter(Plot::getSlot),
            PlotType.CODEC.fieldOf("plotType").forGetter(Plot::getPlotType)
    ).apply(instance, Plot::new));
    private int section;
    private int slot;
    private PlotType plotType;

    public Plot(int section, int slot, PlotType plotType) {
        this.section = section;
        this.slot = slot;
        this.plotType = plotType;
    }

    public static Plot fromJson(String json) {
        JsonOps jsonOps = JsonOps.INSTANCE;
        return Plot.CODEC.decode(jsonOps, JsonParser.parseString(json))
                .resultOrPartial(System.err::println)
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Override
    public String toString() {
        JsonOps jsonOps = JsonOps.INSTANCE; // Use the JSON operations for serialization
        return Plot.CODEC.encodeStart(jsonOps, this)
                .resultOrPartial(System.err::println)
                .map(JsonElement::toString)
                .orElse("{}");
    }
}
