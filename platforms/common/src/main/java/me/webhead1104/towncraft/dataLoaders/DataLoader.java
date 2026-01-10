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
package me.webhead1104.towncraft.dataLoaders;

import com.google.common.base.Stopwatch;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.ExcludeFromClassGraph;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Language;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface DataLoader {
    void load();

    default ConfigurationNode getNodeFromFile(@Language("file-reference") String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Could not find resource file: " + path);
            }
            return Towncraft.GSON_CONFIGURATION_LOADER
                    .source(() -> new BufferedReader(new InputStreamReader(stream)))
                    .build()
                    .load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<ConfigurationNode> getNodesFromMultipleFiles(@Language("file-reference") String path) {
        List<ConfigurationNode> list = new ArrayList<>();

        String searchPath = path.startsWith("/") ? path.substring(1) : path;

        try (ScanResult scanResult = new ClassGraph().acceptPaths(searchPath).scan()) {
            for (Resource resource : scanResult.getResourcesWithExtension("json")) {
                String resourcePath = "/" + resource.getPath();
                Towncraft.getLogger().debug("Loading file: {}", resourcePath);

                try (InputStream stream = resource.open()) {
                    ConfigurationNode node = Towncraft.GSON_CONFIGURATION_LOADER
                            .source(() -> new BufferedReader(new InputStreamReader(stream)))
                            .build()
                            .load();
                    list.add(node);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load resource: " + resourcePath, e);
                }
            }
        }
        return list;
    }

    default <T> List<T> getListFromFile(@Language("file-reference") String path, Class<T> clazz) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ConfigurationNode node = getNodeFromFile(path);
        if (!node.isList()) {
            throw new RuntimeException("Node is not list!");
        }
        try {
            List<T> result = node.getList(clazz);
            if (result == null || result.isEmpty()) {
                throw new RuntimeException("List is null or empty for path `" + path + "`!");
            }
            Towncraft.getLogger().debug(
                    "Took {} to load list from path `{}`",
                    stopwatch.stop().elapsed().toMillis(),
                    path);
            return result;
        } catch (SerializationException e) {
            throw new RuntimeException("An error occurred whilst getting a list from file `" + path + "`!", e);
        }
    }

    default <T> List<T> getListFromMultipleFiles(@Language("file-reference") String path, Class<T> clazz) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<ConfigurationNode> nodes = getNodesFromMultipleFiles(path);
        if (nodes == null || nodes.isEmpty()) {
            throw new RuntimeException("List of nodes is null or empty!");
        }
        try {
            List<T> results = new ArrayList<>();
            for (ConfigurationNode node : nodes) {
                List<T> result = node.getList(clazz);
                if (result == null || result.isEmpty()) {
                    throw new RuntimeException("List is null or empty!");
                }
                results.addAll(result);
            }
            Towncraft.getLogger().debug(
                    "Took {} to load list from files from path `{}`",
                    stopwatch.stop().elapsed().toMillis(),
                    path);
            return results;
        } catch (SerializationException e) {
            throw new RuntimeException("An error occurred whilst getting a list from files `" + path + "`!", e);
        }
    }

    @ExcludeFromClassGraph
    interface KeyBasedDataLoader<ResultType> extends DataLoader {
        ResultType get(Key key);

        Collection<Key> keys();

        Collection<ResultType> values();
    }

    @ExcludeFromClassGraph
    interface IntegerBasedDataLoader<ResultType> extends DataLoader {

        List<ResultType> list();

        ResultType get(int key);
    }
}
