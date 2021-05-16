package com.readutf.mauth.database.impl;

import com.readutf.mauth.database.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.*;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

public class Mongo implements Database {

    String host, username, password, database;
    int port;

    @Getter
    MongoDatabase db;
    MongoClient mongoClient;

    ServerAddress serverAddress;

    public Mongo(String host, int port, String database) {
        this.host = host;
        this.database = database;
        this.port = port;

        connect();
    }

    public Mongo(String host, String username, String password, String database, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;

        connect();
    }

    public void connect() {
        ServerAddress serverAddress = new ServerAddress(host, port);
        if(password != null) {
            MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
            mongoClient = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(serverAddress);
        }
        db = mongoClient.getDatabase(database);
    }

    @Override
    public String getPreviousIp(UUID uuid) {
        Document document = getCollection().find(new BasicDBObject("uuid", uuid.toString())).first();
        if(document == null) {

            return null;
        }
        return document.getString("ip");
    }

    @Override
    public void setPreviousIp(UUID uuid, String ip) {
        Document document = getCollection().find(new BasicDBObject("uuid", uuid.toString())).first();
        if(document == null) {
            document = new Document("uuid", uuid.toString());
            document.put("ip", ip);
            getCollection().insertOne(document);
        } else {
            document.put("ip", ip);

            getCollection().replaceOne(new Document("uuid", uuid.toString()), document);
        }
    }

    @Override
    public boolean isSet(UUID uuid) {
        return getCollection().find(new BasicDBObject("uuid", uuid.toString())).first() != null;
    }

    @Override
    public void removeIp(UUID uuid) {
        if(isSet(uuid)) {
            getCollection().deleteOne(new Document("uuid", uuid.toString()));
        }
    }

    public MongoCollection<Document> getCollection() {
        return db.getCollection("uauth");
    }

}
