package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@Getter
@Setter
@ConfigSerializable
@AllArgsConstructor
public class BuildingType {
    private String key;
    private List<BuildMenu.Building> buildings;
    private transient String type;
}
