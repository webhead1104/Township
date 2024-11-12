package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Level {
    private long xpNeeded;
    private long coinsGiven;
    private long cashGiven;
}
