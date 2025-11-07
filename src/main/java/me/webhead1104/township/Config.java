package me.webhead1104.township;

import lombok.Getter;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Getter
@Setter
public class Config {
    @Setting("version")
    private final int version = 1;
    @Setting("database")
    private final DatabaseConfig database = new DatabaseConfig();

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
        private String username = "township";
        @Setting("password")
        private String password = "";
        @Setting("database")
        private String database = "township";
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
        private final String username = "township";
        @Setting("password")
        private final String password = "";
        @Setting("database")
        private final String database = "township";
        @Setting("collection")
        private final String collection = "township";
        @Setting("uri")
        private final String uri = "";
    }
}
