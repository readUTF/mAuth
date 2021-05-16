package com.readutf.mauth;


import com.readutf.fedex.FedEx;
import com.readutf.mauth.bot.mAuthBot;
import com.readutf.mauth.database.Database;
import com.readutf.mauth.database.impl.FlatFile;
import com.readutf.mauth.database.impl.Mongo;
import com.readutf.mauth.database.impl.MySQL;
import com.readutf.mauth.listener.ErrorJoin;
import com.readutf.mauth.listener.PlayerJoin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

@Getter
public class mAuth extends JavaPlugin {

    @Getter private static mAuth instance;

    Database database;
    FedEx fedEx;
    mAuthBot mAuthBot;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        String botKey = getConfig().getString("bot.key");

        if(botKey == null || botKey.equalsIgnoreCase("")) {
            Bukkit.getLogger().log(Level.FINE, "Discord key was not set in the config.yml");
            Bukkit.getPluginManager().registerEvents(new ErrorJoin(), this);
            return;
        }



        try {
            mAuthBot = new mAuthBot(this);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error loading JDA");
            e.printStackTrace();
            Bukkit.getPluginManager().registerEvents(new ErrorJoin(), this);
            return;
        }

        Arrays.asList(
            new PlayerJoin()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        FileConfiguration config = getConfig();

        if(getConfig().getBoolean("mysql.enabled")) {
            MySQL mySQL = new MySQL(config.getString("mysql.host"),
                    config.getString("mysql.database"),
                    config.getString("mysql.username"),
                    config.getString("mysql.password"),
                    config.getInt("mysql.port"));
            mySQL.connect();
            database = mySQL;
        } else if(getConfig().getBoolean("mongodb.enabled")) {
            database = new Mongo("127.0.0.1", 27017, "mauth");
        } else {
            database = new FlatFile();
        }


    }

    @Override
    public void onDisable() {
    }



}
