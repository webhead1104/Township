package me.webhead1104.township.registries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public class ExpansionRegistry extends Registry<ExpansionRegistry.Expansion> {

    @Getter
    @Setter
    @AllArgsConstructor
    @ConfigSerializable
    public static class Expansion {
        @Setting("population_needed")
        private int populationNeeded;
        @Setting("coins_needed")
        private int coinsNeeded;
        @Setting("time")
        private String time;
        @Setting("xp")
        private int xp;
        @Setting("tools_needed")
        private ToolsNeeded toolsNeeded;

        @ConfigSerializable
        public static class ToolsNeeded {
            @Setting("axe")
            private int axe;
            @Setting("shovel")
            private int shovel;
            @Setting("saw")
            private int saw;
        }
    }
}
