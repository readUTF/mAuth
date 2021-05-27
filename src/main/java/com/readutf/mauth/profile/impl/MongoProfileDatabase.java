package com.readutf.mauth.profile.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.profile.ProfileDatabase;
import com.readutf.mauth.utils.timeprofiler.TimeProfiler;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

public class MongoProfileDatabase extends ProfileDatabase {

    String host;
    String username;
    String password;
    String database;
    int port;

    @Getter
    MongoDatabase db;
    MongoClient mongoClient;

    ServerAddress serverAddress;

    public MongoProfileDatabase(String host, int port, String username, String password, String database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;

        connect();
    }

    public MongoProfileDatabase(String host, int port, String database) {
        this.host = host;
        this.database = database;
        this.port = port;

        connect();
    }

    public void connect() {
        ServerAddress serverAddress = new ServerAddress(host, port);
        if (password != null) {
            MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
            mongoClient = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(serverAddress);
        }
        db = mongoClient.getDatabase(database);
    }


    @Override
    public Profile getProfile(UUID uuid) {



        Profile profile = getProfiles().stream().filter(profile1 -> profile1.getUuid().equals(uuid)).findFirst().orElse(null);


        if (profile == null) {
            TimeProfiler timeProfiler = new TimeProfiler();
            profile = new Profile(uuid);
            Document document = getDocument(uuid);

            timeProfiler.addCheckPoint("1");

            if (document.containsKey("previousIp")) {
                profile.setIp(document.getString("previousIp"));
            }
            if (document.containsKey("discord")) {
                profile.setDiscordId(document.getString("discord"));
            }
            if (document.containsKey("deactivated")) {
                profile.setDeactivated(document.getBoolean("deactivated"));
            }
            if (document.containsKey("verifyAddress")) {
                profile.setVerifyAddress(document.getBoolean("verifyAddress"));
            }
            if(document.containsKey("2fa")) {
                profile.setTfa(document.getBoolean("2fa"));
            }
            if(document.containsKey("gAuth")) {
                profile.setGAuthKey(document.getString("gAuth"));
            }

            getProfiles().add(profile);

            timeProfiler.printProfile();

        }



        return profile;
    }

    @Override
    public void saveProfile(Profile profile) {
        Document document = getDocument(profile.getUuid());
        if (profile.getIp() != null) {
            document.put("previousIp", profile.getIp());
        }
        if (profile.getDiscordId() != null) {
            document.put("discord", profile.getDiscordId());
        }
        if(profile.getGAuthKey() != null) {
            document.put("gAuth", profile.getGAuthKey());
        }
        document.put("deactivated", profile.isDeactivated());
        document.put("verifyAddress", profile.isVerifyAddress());
        document.put("2fa", profile.isTfa());
        getCollection().replaceOne(new Document("uuid", profile.getUuid().toString()), document);
    }

    public Document getDocument(UUID uuid) {
        Document document = getCollection().find(new Document("uuid", uuid.toString())).first();
        if (document == null) {
            document = new Document("uuid", uuid.toString());
            getCollection().insertOne(document);
            return document;
        }
        return document;
    }

    public MongoCollection<Document> getCollection() {
        return db.getCollection("uauth");
    }

}
