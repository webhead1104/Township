package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ConfigSerializable
public class BuildMenu {
    private String key;
    private List<String> buildings;
}
