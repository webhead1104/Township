package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class BarnUpgrade {
    public static final @NotNull Codec<BarnUpgrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(BarnUpgrade::getId),
            Codec.INT.fieldOf("toolsNeeded").forGetter(BarnUpgrade::getToolsNeeded),
            Codec.INT.fieldOf("barnStorage").forGetter(BarnUpgrade::getBarnStorage)
    ).apply(instance, BarnUpgrade::new));
    private int id;
    private int toolsNeeded;
    private int barnStorage;

    public BarnUpgrade(int id, int toolsNeeded, int barnStorage) {
        this.id = id;
        this.toolsNeeded = toolsNeeded;
        this.barnStorage = barnStorage;
    }
}
