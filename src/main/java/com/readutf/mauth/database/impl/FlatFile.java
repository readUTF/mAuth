package com.readutf.mauth.database.impl;


import com.readutf.mauth.database.Database;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.utils.ConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class FlatFile implements Database {

    ConfigUtil playerDataConfig;
    FileConfiguration playerData;

    public FlatFile() {
        playerDataConfig = new ConfigUtil(mAuth.getInstance(), "playerdata.yml", null);
        playerData = playerDataConfig.getConfiguration();
    }


    @Override
    public String getPreviousIp(UUID uuid) {
        return playerData.isSet(uuid.toString()) ? playerData.getString(uuid.toString()) : null;
    }

    @Override
    public void setPreviousIp(UUID uuid, String country) {
        playerData.set(uuid.toString(), country);
        playerDataConfig.saveConfig();
    }

    @Override
    public boolean isSet(UUID uuid) {
        return playerData.isSet(uuid.toString());
    }

    @Override
    public void removeIp(UUID uuid) {
        playerData.set(uuid.toString(), null);
    }
}
