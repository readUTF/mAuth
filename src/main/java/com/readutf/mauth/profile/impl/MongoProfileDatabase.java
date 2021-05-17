package com.readutf.mauth.profile.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.profile.ProfileDatabase;
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

        System.out.println(getProfiles());

        Profile profile = getProfiles().stream().filter(profile1 -> profile1.getUuid().equals(uuid)).findFirst().orElse(null);


        if (profile == null) {
            Document document = getDocument(uuid);
            String previousIp = null, discord = null;
            boolean deactivated = false;
            boolean verifyAddress = false;
            boolean tfa = false;


            if (document.containsKey("previousIp")) {
                previousIp = document.getString("previousIp");
            }
            if (document.containsKey("discord")) {
                discord = document.getString("discord");
            }
            if (document.containsKey("deactivated")) {
                deactivated = document.getBoolean("deactivated");
            }
            if (document.containsKey("verifyAddress")) {
                verifyAddress = document.getBoolean("verifyAddress");
            }
            if(document.containsKey("2fa")) {
                tfa = document.getBoolean("2fa");
            }


            profile = new Profile(uuid,
                    previousIp,
                    discord,
                    deactivated,
                    verifyAddress,
                    tfa);
            getProfiles().add(profile);
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
