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
package me.webhead1104.menuDesigner;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class AssetCache {
    private final Path cacheDir;
    private final Map<String, String> etags = new ConcurrentHashMap<>();
    private final Map<String, String> lastModified = new ConcurrentHashMap<>();

    public AssetCache(Path cacheDir) {
        this.cacheDir = cacheDir;
    }

    public Path cachedFile(String key) {
        return cacheDir.resolve(key);
    }

    public byte[] getOrDownload(String key, String url) throws IOException {
        Path target = cachedFile(key);
        Files.createDirectories(target.getParent());

        HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);
        if (Files.exists(target)) {
            String etag = etags.get(key);
            String lm = lastModified.get(key);
            if (etag != null) conn.setRequestProperty("If-None-Match", etag);
            if (lm != null) conn.setRequestProperty("If-Modified-Since", lm);
        }
        conn.connect();
        int code = conn.getResponseCode();
        if (code == 304 && Files.exists(target)) {
            return Files.readAllBytes(target);
        }
        if (code != 200) {
            if (Files.exists(target)) {
                return Files.readAllBytes(target);
            }
            throw new IOException("HTTP " + code + " for " + url);
        }
        try (InputStream in = conn.getInputStream()) {
            Files.createDirectories(target.getParent());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        String etag = conn.getHeaderField("ETag");
        String lm = conn.getHeaderField("Last-Modified");
        if (etag != null) etags.put(key, etag);
        if (lm != null) lastModified.put(key, lm);
        return Files.readAllBytes(target);
    }

    public String getOrDownloadText(String key, String url) throws IOException {
        return new String(getOrDownload(key, url));
    }
}
