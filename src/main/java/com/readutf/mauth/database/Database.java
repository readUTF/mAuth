package com.readutf.mauth.database;

import java.util.UUID;

public interface Database {

    String getPreviousIp(UUID uuid);
    void setPreviousIp(UUID uuid, String ip);
    boolean isSet(UUID uuid);
    void removeIp(UUID uuid);

}
