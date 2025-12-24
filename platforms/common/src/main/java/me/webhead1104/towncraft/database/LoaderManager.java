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
package me.webhead1104.towncraft.database;

import lombok.Getter;
import me.webhead1104.towncraft.Config;
import me.webhead1104.towncraft.Towncraft;

import java.io.File;
import java.io.IOException;

@Getter
public class LoaderManager {
    private final UserLoader userLoader;

    public LoaderManager() {
        Config config = Towncraft.getConfig();
        Config.DatabaseConfig databaseConfig = config.getDatabase();

        userLoader = switch (databaseConfig.getDatabaseType()) {
            case FILE -> new FileLoader(new File(Towncraft.getDataFolder(), databaseConfig.getFileConfig().getPath()));
            case MYSQL -> {
                Config.MysqlConfig mysqlConfig = databaseConfig.getMysqlConfig();
                yield new MysqlLoader(
                        mysqlConfig.getSqlUrl(),
                        mysqlConfig.getHost(), mysqlConfig.getPort(),
                        mysqlConfig.getDatabase(), mysqlConfig.isUseSsl(),
                        mysqlConfig.getUsername(), mysqlConfig.getPassword()
                );
            }
            case MONGODB -> {
                Config.MongoDBConfig mongoConfig = databaseConfig.getMongodbConfig();
                yield new MongoLoader(
                        mongoConfig.getDatabase(),
                        mongoConfig.getCollection(),
                        mongoConfig.getUsername(),
                        mongoConfig.getPassword(),
                        mongoConfig.getAuthSource(),
                        mongoConfig.getHost(),
                        mongoConfig.getPort(),
                        mongoConfig.getUri()
                );
            }
        };
        if (userLoader instanceof UpdatableUserLoader updatableLoader) {
            try {
                updatableLoader.update();
            } catch (UpdatableUserLoader.NewerStorageException e) {
                Towncraft.getLogger().error("Data source {} version is {}, while this loader version only supports up to version {}.",
                        databaseConfig.getDatabaseType().name(), e.getStorageVersion(), e.getImplementationVersion(), e);
            } catch (IOException e) {
                Towncraft.getLogger().error("Failed to update data source {}", databaseConfig.getDatabaseType().name(), e);
            }
        }
    }
}
