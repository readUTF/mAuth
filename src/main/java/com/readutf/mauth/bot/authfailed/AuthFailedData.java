package com.readutf.mauth.bot.authfailed;

import com.google.gson.JsonObject;
import com.readutf.fedex.interfaces.Parcel;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthFailedData extends Parcel {

    UUID uuid;
    String newIp;
    String oldIp;
    String username;

    public AuthFailedData(UUID uuid, String oldIp, String newIp, String username) {
        this.uuid = uuid;
        this.newIp = newIp;
        this.oldIp = oldIp;
        this.username = username;
    }

    @Override
    public String getName() {
        return "AuthFailed";
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("newIp", newIp);
        jsonObject.addProperty("oldIp", oldIp);
        jsonObject.addProperty("username", username);
        return jsonObject;
    }
}
