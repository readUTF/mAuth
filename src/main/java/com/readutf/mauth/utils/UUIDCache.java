package com.readutf.mauth.utils;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

public class UUIDCache {

    private static OkHttpClient httpClient = new OkHttpClient();


    public static UUID getUUID(String username) {

        UUID id;

        Request request = new Request.Builder()
                .url("https://api.mojang.com/users/profiles/minecraft/" + username)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
            id = UUID.fromString(jsonObject.get("id").getAsString().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    public static String getUsername(UUID id) {
        Request request = new Request.Builder().url("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString()).build();
        try (Response response = httpClient.newCall(request).execute()) {
            JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
            return jsonObject.get("name").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
