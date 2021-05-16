package com.readutf.mauth.bot.authfailed;

import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthFailedData {

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

}
