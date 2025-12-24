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
package me.webhead1104.towncraft;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.database.LoaderManager;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.concurrent.TimeUnit;

@ConfigSerializable
@Getter
@Setter
public class Config {
    private static final TowncraftPlatform platform = TowncraftPlatformManager.getPlatform();
    private static final File OLD_PLUGIN_DIR = new File("plugins", "Township");
    private static final File CONFIG_FILE = new File(platform.getDataFolder(), "config.yml");
    @Setting("version")
    private final int version = 1;
    @Setting("database")
    private final DatabaseConfig database = new DatabaseConfig();

    public static void saveConfig() {
        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(CONFIG_FILE)
                    .nodeStyle(NodeStyle.BLOCK).headerMode(HeaderMode.PRESERVE).build();
            loader.save(loader.createNode().set(TowncraftPlatformManager.getConfig()));
        } catch (Exception e) {
            platform.getLogger().error("An error occurred whilest saving the config!", e);
        }
    }

    public static void loadConfig() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            if (OLD_PLUGIN_DIR.exists()) {
                if (!OLD_PLUGIN_DIR.renameTo(platform.getDataFolder())) {
                    platform.getLogger().error("Could not rename config file!");
                }
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .file(CONFIG_FILE)
                    .commentsEnabled(true)
                    .nodeStyle(NodeStyle.BLOCK)
                    .headerMode(HeaderMode.PRESERVE)
                    .build();

            if (!CONFIG_FILE.exists()) {
                TowncraftPlatformManager.setConfig(new Config());
                saveConfig();
                platform.getLogger().info("Created new config file");
            }

            ConfigurationNode node = loader.load();

            if (node.node("version").getInt() == 0) {
                platform.getLogger().info("Migrating your old config file!");
                ConfigurationNode oldConfigMysql = node.node("mysql");

                Config.MysqlConfig mysqlConfig = new Config.MysqlConfig();
                mysqlConfig.setHost(oldConfigMysql.node("host").getString());
                mysqlConfig.setPort(oldConfigMysql.node("port").getInt());
                mysqlConfig.setDatabase(oldConfigMysql.node("database").getString());
                mysqlConfig.setUsername(oldConfigMysql.node("username").getString());
                mysqlConfig.setPassword(oldConfigMysql.node("password").getString());
                mysqlConfig.setUseSsl(oldConfigMysql.node("use_ssl").getBoolean());
                TowncraftPlatformManager.setConfig(node.get(Config.class));

                if (TowncraftPlatformManager.getConfig() != null) {
                    TowncraftPlatformManager.getConfig().getDatabase().setMysqlConfig(mysqlConfig);
                }

                saveConfig();
                platform.getLogger().info("Config migration complete");
            } else {
                TowncraftPlatformManager.setConfig(node.get(Config.class));
            }

            TowncraftPlatformManager.setLoaderManager(new LoaderManager());
            platform.getLogger().info("Loaded config in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            platform.getLogger().error("Could not load config!", e);
        }
    }

    public enum DatabaseType {
        FILE,
        MYSQL,
        MONGODB
    }

    @Getter
    @ConfigSerializable
    public static class DatabaseConfig {
        @Setting("databaseType")
        @Comment("""
                Database storage type Options: FILE, MYSQL, MONGODB
                """)
        private final DatabaseType databaseType = DatabaseType.FILE;
        @Setting("file")
        private final FileConfig fileConfig = new FileConfig();
        @Setting("mongodb")
        private final MongoDBConfig mongodbConfig = new MongoDBConfig();
        @Setter
        @Setting("mysql")
        private MysqlConfig mysqlConfig = new MysqlConfig();
    }

    @Getter
    @ConfigSerializable
    public static class FileConfig {
        @Setting("path")
        private final String path = "user_data";
    }

    @Getter
    @Setter
    @ConfigSerializable
    public static class MysqlConfig {
        @Setting("host")
        private String host = "localhost";
        @Setting("port")
        private int port = 3306;
        @Setting("username")
        private String username = "towncraft";
        @Setting("password")
        private String password = "";
        @Setting("database")
        private String database = "towncraft";
        @Setting("usessl")
        private boolean useSsl = false;
        @Setting("sqlUrl")
        private String sqlUrl = "jdbc:mysql://{host}:{port}/{database}?autoReconnect=true&allowMultiQueries=true&useSSL={usessl}&allowPublicKeyRetrieval=true";
    }

    @Getter
    @ConfigSerializable
    public static class MongoDBConfig {
        @Setting("host")
        private final String host = "localhost";
        @Setting("port")
        private final int port = 27017;
        @Setting("auth")
        private final String authSource = "admin";
        @Setting("username")
        private final String username = "towncraft";
        @Setting("password")
        private final String password = "";
        @Setting("database")
        private final String database = "towncraft";
        @Setting("collection")
        private final String collection = "towncraft";
        @Setting("uri")
        private final String uri = "";
    }
}
