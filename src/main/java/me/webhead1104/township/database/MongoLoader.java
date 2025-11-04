package me.webhead1104.township.database;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import me.webhead1104.township.exceptions.UnknownUserException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoLoader implements UserLoader {
    private final MongoClient client;
    private final String database;
    private final String collection;

    public MongoLoader(String database, String collection, @Nullable String username, @Nullable String password,
                       @Nullable String authSource, @Nullable String host, @Nullable Integer port, @Nullable String uri) throws MongoException {
        this.database = database;
        this.collection = collection;

        String authParams = username != null && password != null ? username + ":" + password + "@" : "";
        String parsedAuthSource = authSource != null ? "/?authSource=" + authSource : "";
        String parsedUri = uri != null && !uri.isBlank() ? uri : "mongodb://" + authParams + host + ":" + port + parsedAuthSource;

        this.client = MongoClients.create(parsedUri);

        init();
    }

    private void init() {
        MongoDatabase mongoDatabase = client.getDatabase(database);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

        mongoCollection.createIndex(Indexes.ascending("uuid"), new IndexOptions().unique(true));
    }

    @Override
    public String readUser(UUID userUUID) throws UnknownUserException, IOException {
        try {
            MongoDatabase mongoDatabase = client.getDatabase(database);
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            Document userDoc = mongoCollection.find(Filters.eq("uuid", userUUID.toString())).first();

            if (userDoc == null) {
                throw new UnknownUserException(userUUID);
            }

            GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, collection + "_gridfs");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bucket.downloadToStream(userUUID.toString(), stream);

            return stream.toString();
        } catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public boolean userExists(UUID userUUID) throws IOException {
        try {
            MongoDatabase mongoDatabase = client.getDatabase(database);
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            Document userDoc = mongoCollection.find(Filters.eq("uuid", userUUID.toString())).first();

            return userDoc != null;
        } catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public List<UUID> listUsers() throws IOException {
        List<UUID> userList = new ArrayList<>();

        try {
            MongoDatabase mongoDatabase = client.getDatabase(database);
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            try (MongoCursor<Document> documents = mongoCollection.find().cursor()) {
                while (documents.hasNext()) {
                    userList.add(UUID.fromString(documents.next().getString("uuid")));
                }
            }
        } catch (MongoException ex) {
            throw new IOException(ex);
        }

        return userList;
    }

    @Override
    public void saveUser(UUID userUUID, String userData) throws IOException {
        try {
            MongoDatabase mongoDatabase = client.getDatabase(database);
            GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, collection + "_gridfs");

            GridFSFile oldFile = bucket.find(Filters.eq("filename", userUUID.toString())).first();
            if (oldFile != null) {
                bucket.delete(oldFile.getObjectId());
            }

            bucket.uploadFromStream(userUUID.toString(), new ByteArrayInputStream(userData.getBytes()));

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            Bson query = Filters.eq("uuid", userUUID.toString());
            mongoCollection.updateOne(
                    query,
                    new Document().append("$set", new Document("uuid", userUUID.toString())),
                    new UpdateOptions().upsert(true)
            );
        } catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void deleteUser(UUID userUUID) throws IOException, UnknownUserException {
        try {
            MongoDatabase mongoDatabase = client.getDatabase(database);
            GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, collection + "_gridfs");

            GridFSFile file = bucket.find(Filters.eq("filename", userUUID.toString())).first();
            if (file == null) {
                throw new UnknownUserException(userUUID);
            }
            bucket.delete(file.getObjectId());

            for (GridFSFile backupFile : bucket.find(Filters.eq("filename", userUUID + "_backup"))) {
                bucket.delete(backupFile.getObjectId());
            }

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            mongoCollection.deleteOne(Filters.eq("uuid", userUUID.toString()));
        } catch (MongoException ex) {
            throw new IOException(ex);
        }
    }
}
