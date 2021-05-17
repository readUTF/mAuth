package com.readutf.mauth.bot;

import com.readutf.mauth.bot.commands.DiscordCommands;
import com.readutf.mauth.bot.listeners.ReactionAdd;
import com.readutf.mauth.bot.listeners.SyncMessageListener;
import com.readutf.mauth.bot.messages.MessageHandler;
import com.readutf.mauth.mAuth;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class mAuthBot implements Listener {

    @Getter
    private static mAuthBot instance;

    @Getter
    private JDA jda;

    @Getter
    MessageHandler messageHandler;


    public mAuthBot(mAuth javaPlugin) throws Exception {
        instance = this;

        FileConfiguration configuration = javaPlugin.getConfig();


        jda = JDABuilder.createDefault(configuration.getString("bot.key"))
                .addEventListeners(new ReactionAdd(), new DiscordCommands(), new SyncMessageListener())
                .build();

        messageHandler = new MessageHandler();




        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    @EventHandler
    public void pluginDisable(PluginDisableEvent e) {
        jda.shutdownNow();
    }


}
