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
