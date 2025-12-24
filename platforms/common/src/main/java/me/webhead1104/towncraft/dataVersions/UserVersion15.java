/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.data.TileSize;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Keep
public class UserVersion15 implements DataVersion {
    private static final int EVENT_CENTER_SECTION = 28;
    private static final int EVENT_CENTER_START_SLOT = 3;
    private static final TileSize EVENT_CENTER_SIZE = new TileSize(3, 5);

    @Override
    public ConfigurationTransformation getTransformation() {
        return root -> {
            ConfigurationNode worldSection = root.node("world", "world-map", String.valueOf(EVENT_CENTER_SECTION));

            if (worldSection.virtual()) {
                return;
            }

            ConfigurationNode slotMap = worldSection.node("slot-map");

            if (slotMap.virtual()) {
                return;
            }

            List<Integer> eventCenterSlots = EVENT_CENTER_SIZE.toList(EVENT_CENTER_START_SLOT);

            Set<BuildingInfo> affectedBuildings = new HashSet<>();

            for (Integer slot : eventCenterSlots) {
                ConfigurationNode tileNode = slotMap.node(String.valueOf(slot));

                if (tileNode.virtual()) {
                    continue;
                }

                String tileClass = tileNode.node("class").getString();

                if (tileClass != null && tileClass.equals("StaticWorldTile")) {
                    continue;
                }

                ConfigurationNode propertiesNode = tileNode.node("properties");
                ConfigurationNode buildingTypeNode = propertiesNode.node("buildingType");
                ConfigurationNode buildingSlotNode = propertiesNode.node("buildingSlot");

                if (!buildingTypeNode.virtual() && !buildingSlotNode.virtual()) {
                    String buildingType = buildingTypeNode.getString();
                    int buildingSlot = buildingSlotNode.getInt(-1);

                    if (buildingType != null && buildingSlot != -1) {
                        affectedBuildings.add(new BuildingInfo(buildingType, buildingSlot));
                    }
                }
            }

            for (BuildingInfo building : affectedBuildings) {
                for (int slot = 0; slot < 54; slot++) {
                    ConfigurationNode tileNode = slotMap.node(String.valueOf(slot));

                    if (tileNode.virtual()) {
                        continue;
                    }

                    ConfigurationNode propertiesNode = tileNode.node("properties");
                    String buildingType = propertiesNode.node("buildingType").getString();
                    int buildingSlot = propertiesNode.node("buildingSlot").getInt(-1);

                    if (building.buildingType.equals(buildingType) && building.buildingSlot == buildingSlot) {
                        tileNode.node("class").raw("StaticWorldTile");
                        tileNode.node("properties").raw(null);
                        ConfigurationNode newProps = tileNode.node("properties");
                        newProps.node("material").raw("minecraft:grass_block");
                    }
                }

                ConfigurationNode purchasedBuildingsNode = root.node("purchased-buildings", "purchased-buildings", building.buildingType);

                if (purchasedBuildingsNode.virtual()) {
                    purchasedBuildingsNode.raw(new ArrayList<>());
                }

                boolean found = false;
                for (ConfigurationNode purchasedNode : purchasedBuildingsNode.childrenList()) {
                    int slot = purchasedNode.node("slot").getInt(-1);
                    int section = purchasedNode.node("section").getInt(-1);

                    if (slot == building.buildingSlot && section == EVENT_CENTER_SECTION) {
                        purchasedNode.node("placed").raw(false);
                        purchasedNode.node("section").raw(-1);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    ConfigurationNode newBuilding = purchasedBuildingsNode.appendListNode();
                    newBuilding.node("slot").raw(building.buildingSlot);
                    newBuilding.node("section").raw(-1);
                    newBuilding.node("placed").raw(false);
                    newBuilding.node("building-type").raw(building.buildingType);
                }
            }

            for (Integer slot : eventCenterSlots) {
                ConfigurationNode tileNode = slotMap.node(String.valueOf(slot));
                tileNode.node("class").raw("EventCenterTile");
                tileNode.node("properties").raw(null);
            }
        };
    }

    @Override
    public int getVersion() {
        return 15;
    }

    private record BuildingInfo(String buildingType, int buildingSlot) {
    }
}