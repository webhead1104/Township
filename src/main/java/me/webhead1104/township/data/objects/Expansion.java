package me.webhead1104.township.data.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class Expansion {
    public static final @NotNull Codec<Expansion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("section").forGetter(Expansion::getSection),
            Codec.INT.fieldOf("slot").forGetter(Expansion::getSlot),
            Codec.INT.fieldOf("price").forGetter(Expansion::getPrice),
            Codec.INT.fieldOf("populationNeeded").forGetter(Expansion::getPopulationNeeded)
    ).apply(instance, Expansion::new));
    private int section;
    private int slot;
    private int price;
    private int populationNeeded;

    public Expansion(int section, int slot, int price, int populationNeeded) {
        this.section = section;
        this.slot = slot;
        this.price = price;
        this.populationNeeded = populationNeeded;
    }

    public static Expansion fromJson(String json) {
        JsonOps jsonOps = JsonOps.INSTANCE;
        return Expansion.CODEC.decode(jsonOps, JsonParser.parseString(json))
                .resultOrPartial(System.err::println)
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Override
    public String toString() {
        JsonOps jsonOps = JsonOps.INSTANCE;
        return Expansion.CODEC.encodeStart(jsonOps, this)
                .resultOrPartial(System.err::println)
                .map(JsonElement::toString)
                .orElse("{}");
    }
}
