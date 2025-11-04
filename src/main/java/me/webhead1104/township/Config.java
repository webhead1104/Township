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
    private int version = 1;
    @Setting("database")
    private DatabaseConfig database = new DatabaseConfig();

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
        private DatabaseType databaseType = DatabaseType.FILE;
        @Setting("file")
        private FileConfig fileConfig = new FileConfig();
        @Setter
        @Setting("mysql")
        private MysqlConfig mysqlConfig = new MysqlConfig();
        @Setting("mongodb")
        private MongoDBConfig mongodbConfig = new MongoDBConfig();
    }

    @Getter
    @ConfigSerializable
    public static class FileConfig {
        @Setting("path")
        private String path = "user_data";
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
        private String host = "localhost";
        @Setting("port")
        private int port = 27017;
        @Setting("auth")
        private String authSource = "admin";
        @Setting("username")
        private String username = "township";
        @Setting("password")
        private String password = "";
        @Setting("database")
        private String database = "township";
        @Setting("collection")
        private String collection = "township";
        @Setting("uri")
        private String uri = "";
    }
}
