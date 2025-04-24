package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Level {
    private long xpNeeded;
    private long coinsGiven;
    private long cashGiven;

    public Level(long xpNeeded, long coinsGiven, long cashGiven) {
        this.xpNeeded = xpNeeded;
        this.coinsGiven = coinsGiven;
        this.cashGiven = cashGiven;
    }
}
