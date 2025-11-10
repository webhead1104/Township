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
        Config config = Towncraft.getTowncraftConfig();
        Config.DatabaseConfig databaseConfig = config.getDatabase();

        userLoader = switch (databaseConfig.getDatabaseType()) {
            case FILE ->
                    new FileLoader(new File(Towncraft.getInstance().getDataFolder(), databaseConfig.getFileConfig().getPath()));
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
                Towncraft.logger.error("Data source {} version is {}, while this loader version only supports up to version {}.",
                        databaseConfig.getDatabaseType().name(), e.getStorageVersion(), e.getImplementationVersion(), e);
            } catch (IOException e) {
                Towncraft.logger.error("Failed to update data source {}", databaseConfig.getDatabaseType().name(), e);
            }
        }
    }
}
