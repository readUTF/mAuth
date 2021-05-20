package com.readutf.mauth;


import com.readutf.mauth.bot.mAuthBot;
import com.readutf.mauth.commands.*;
import com.readutf.mauth.listener.*;
import com.readutf.mauth.profile.ProfileDatabase;
import com.readutf.mauth.profile.impl.MongoProfileDatabase;
import com.readutf.mauth.utils.SecureKeyGenerator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

@Getter
public class mAuth extends JavaPlugin {

    @Getter
    private static mAuth instance;

    ProfileDatabase profileDatabase;
    mAuthBot mAuthBot;

    String gAuthSecret;

    @Override
    public void onEnable() {

        Thread thread = new Thread(this::init, "Main Thread");
        thread.setUncaughtExceptionHandler((t, e) -> {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getLogger().log(Level.SEVERE, "mAuth failed to load: " + e.getMessage());
        });
        thread.start();


    }

    public void init() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        String botKey = getConfig().getString("bot.key");

        if (botKey == null || botKey.equalsIgnoreCase("")) {
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
                new PlayerJoin(),
                new PlayerQuit(),
                new AuthListener(),
                new PlayerChat()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        if (!getConfig().isSet("gauth-secret")) {
            gAuthSecret = SecureKeyGenerator.generateSecretKey();
            getConfig().set("gauth-secret", gAuthSecret);
        }


        getCommand("verifyaddress").setExecutor(new VerifyAddressCommand());
        getCommand("deactivate").setExecutor(new DeactivateCommand());
        getCommand("reactivate").setExecutor(new ReActivateCommand());
        getCommand("sync").setExecutor(new SyncCommand());
        getCommand("2fa").setExecutor(new Toggle2FACommand());

        FileConfiguration config = getConfig();

//        if(getConfig().getBoolean("mysql.enabled")) {
//            MySQL mySQL = new MySQL(config.getString("mysql.host"),
//                    config.getString("mysql.database"),
//                    config.getString("mysql.username"),
//                    config.getString("mysql.password"),
//                    config.getInt("mysql.port"));
//            mySQL.connect();
//            database = mySQL;
//        } else
//        if(getConfig().getBoolean("mongodb.enabled")) {
//            database = new Mongo(config.getString("mongodb.host"), config.getInt("mongodb.port"), config.getString("mongodb.database"));
//        } else {
//            database = new FlatFile();
//        }

        profileDatabase = new MongoProfileDatabase(config.getString("mongodb.host"), config.getInt("mongodb.port"), config.getString("mongodb.database"));
    }

    @Override
    public void onDisable() {
    }



}
