package com.readutf.mauth.listener;

import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.messages.MessageHandler;
import com.readutf.mauth.database.Database;
import com.readutf.mauth.mAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if(!player.hasPermission("mauth.verify")) return;

        UUID uuid = player.getUniqueId();
        String address = player.getAddress().getHostString();
        Database database = mAuth.getInstance().getDatabase();


        Bukkit.getLogger().log(Level.SEVERE, "Verifying connection... ");

        if (!database.isSet(uuid)) {

            database.setPreviousIp(uuid, address);
            Bukkit.getLogger().log(Level.SEVERE, "Connect verified... [First Join]");
            return;
        }
        String lastAddress = database.getPreviousIp(uuid);
        if (lastAddress.equalsIgnoreCase("disabled")) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&',"&cYour account has been disabled for suspecious activity \nplease contact an administrator" ));
            return;
        }

        if (!lastAddress.equalsIgnoreCase(address)) {
            Bukkit.getLogger().log(Level.SEVERE, "Connect unverified [Location Changed]");
            MessageHandler.sendMessage(new AuthFailedData(uuid, lastAddress, address, player.getName()));
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cYou have connected from a different location \nPlease Contact an administrator."));
        }


    }

}
