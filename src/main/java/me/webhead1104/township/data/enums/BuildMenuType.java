package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.data.buildMenus.*;
import me.webhead1104.township.data.interfaces.AbstractBuildMenu;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;

@Getter
public enum BuildMenuType {
    HOUSING(new HousingBuildMenu(), Msg.format("Housing")),
    COMMUNITY(new CommunityBuildMenu(), Msg.format("Community Buildings")),
    FACTORIES(new FactoriesBuildMenu(), Msg.format("Factories")),
    FARMING(new FarmingBuildMenu(), Msg.format("Farming")),
    SPECIAL(new SpecialBuildMenu(), Msg.format("Special"));
    private final AbstractBuildMenu clazz;
    private final Component menuTitle;

    BuildMenuType(AbstractBuildMenu clazz, Component menuTitle) {
        this.clazz = clazz;
        this.menuTitle = menuTitle;
    }
}
