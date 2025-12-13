package me.webhead1104.tools.wikiScraper.model;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@Setter
@ConfigSerializable
public class Price {
    private final String type = "coin";
    private int amount;

    public Price(int amount) {
        this.amount = amount;
    }
}
