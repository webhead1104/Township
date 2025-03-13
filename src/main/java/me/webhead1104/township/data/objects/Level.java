package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class Level {
    public static final @NotNull Codec<Level> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("xpNeeded").forGetter(Level::getXpNeeded),
            Codec.LONG.fieldOf("coinsGiven").forGetter(Level::getCoinsGiven),
            Codec.LONG.fieldOf("cashGiven").forGetter(Level::getCashGiven)
    ).apply(instance, Level::new));
    private long xpNeeded;
    private long coinsGiven;
    private long cashGiven;

    public Level(long xpNeeded, long coinsGiven, long cashGiven) {
        this.xpNeeded = xpNeeded;
        this.coinsGiven = coinsGiven;
        this.cashGiven = cashGiven;
    }
}
