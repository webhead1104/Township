/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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

import me.webhead1104.towncraft.Towncraft;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface DataVersion {
    ConfigurationTransformation getTransformation();

    int getVersion();

    default void runInChildren(ConfigurationNode node, Consumer<ConfigurationNode> consumer, Object... path) {
        node.node(path).childrenMap().values().forEach(consumer);
    }

    default void runInAllWorldSections(ConfigurationNode node, Consumer<ConfigurationNode> consumer) {
        runInChildren(node, consumer, "world", "world-map");
    }

    default void runInAllWorldTiles(ConfigurationNode node, Consumer<ConfigurationNode> consumer) {
        runInAllWorldSections(node, worldSection -> runInChildren(worldSection, consumer, "slot-map"));
    }

    default void replaceBuilding(ConfigurationNode node, ConfigurationNode nodeToMatch) {
        replaceBuilding(node, () -> nodeToMatch);
    }

    default void replaceBuilding(ConfigurationNode node, Supplier<ConfigurationNode> nodeToMatch) {
        ConfigurationNode nodeToMatchNode = nodeToMatch.get();
        runInAllWorldTiles(node, tile -> {
            if (nodesEqual(tile, nodeToMatchNode)) {
                tile.node("class").raw("StaticWorldTile");
                tile.node("properties", "material").raw("minecraft:grass_block");
            }
        });
    }

    default void addNotPlacedPurchasedBuilding(ConfigurationNode rootNode, String type) {
        runInChildren(rootNode, node -> {
            if (node.virtual()) {
                node.raw(new ArrayList<>());
            }
            ConfigurationNode purchasedBuildingNode = node.appendListNode();
            purchasedBuildingNode.node("slot").raw(0);
            purchasedBuildingNode.node("section").raw(-1);
            purchasedBuildingNode.node("placed").raw(false);
            purchasedBuildingNode.node("building-type").raw(type);
        }, "purchased-buildings", "purchased-buildings", type);
    }

    default ConfigurationNode createNode() {
        return Towncraft.GSON_CONFIGURATION_LOADER.build().createNode();
    }

    default boolean nodesEqual(ConfigurationNode node1, ConfigurationNode node2) {
        if (node1.virtual() && node2.virtual()) {
            return true;
        }

        if (node2.virtual()) {
            return false;
        }

        if (node1.virtual()) {
            return true;
        }

        if (node1.isMap() && node2.isMap()) {
            Map<Object, ? extends ConfigurationNode> children1 = node1.childrenMap();
            Map<Object, ? extends ConfigurationNode> children2 = node2.childrenMap();

            for (Map.Entry<Object, ? extends ConfigurationNode> entry : children2.entrySet()) {
                ConfigurationNode child1 = children1.get(entry.getKey());
                if (child1 == null || !nodesEqual(child1, entry.getValue())) {
                    return false;
                }
            }

            return true;
        }

        if (node1.isList() && node2.isList()) {
            List<? extends ConfigurationNode> list1 = node1.childrenList();
            List<? extends ConfigurationNode> list2 = node2.childrenList();

            if (list2.size() > list1.size()) {
                return false;
            }

            for (int i = 0; i < list2.size(); i++) {
                if (!nodesEqual(list1.get(i), list2.get(i))) {
                    return false;
                }
            }

            return true;
        }

        if (node2.isMap() || node2.isList()) {
            return false;
        }

        if (node1.isMap() || node1.isList()) {
            return true;
        }

        return Objects.equals(node1.raw(), node2.raw());
    }
}
