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
package me.webhead1104.menuDesigner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record TextureService(AssetCache cache) {
    public static final String MINECRAFT_VERSION = "1.21.10";
    private static final String REPO_CONTENTS_API =
            "https://api.github.com/repos/InventivetalentDev/minecraft-assets/contents/";
    private static final String REPO_RAW_BASE =
            "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/";

    public TextureService(AssetCache cache) {
        this.cache = Objects.requireNonNull(cache);
    }

    private static String keyFor(String path) {
        return "gh/" + path;
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public Image loadInventoryBackgroundGeneric54() throws IOException {
        String path = "assets/minecraft/textures/gui/container/generic_54.png";
        String key = keyFor(path);
        String url = REPO_RAW_BASE + MINECRAFT_VERSION + "/" + path;
        byte[] data = cache.getOrDownload(key, url);
        return new Image(new ByteArrayInputStream(data));
    }

    public Image loadItem(String itemName) throws IOException {
        String filename = itemName.endsWith(".png") ? itemName : (itemName + ".png");
        String path = "assets/minecraft/textures/item/" + filename;
        String key = keyFor(path);
        String url = REPO_RAW_BASE + MINECRAFT_VERSION + "/" + path;
        byte[] data = cache.getOrDownload(key, url);
        return new Image(new ByteArrayInputStream(data), 16, 16, true, true);
    }

    public List<String> listAllItemTextureNames() throws IOException {
        String dir = "assets/minecraft/textures/item";
        String api = REPO_CONTENTS_API + encode(dir) + "?ref=" + encode(MINECRAFT_VERSION);
        String key = "api/" + MINECRAFT_VERSION + "/" + dir.replace(' ', '_') + ".json";
        String json = cache.getOrDownloadText(key, api);
        List<String> result = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement element : jsonArray) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            JsonElement nameEl = obj.get("name");
            if (nameEl == null || !nameEl.isJsonPrimitive()) {
                continue;
            }

            String name = nameEl.getAsString();
            if (name.endsWith(".png")) {
                result.add(name.substring(0, name.length() - 4));
            }
        }
        result.sort(String::compareTo);
        return result;
    }
}
