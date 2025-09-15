package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
@NoArgsConstructor
public class Level {
    @Setting("xp_needed")
    private int xpNeeded;
    @Setting("coins_given")
    private int coinsGiven;
    @Setting("cash_given")
    private int cashGiven;
}
