package com.readutf.mauth.listener;

import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.database.Database;
import com.readutf.mauth.mAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        String address = e.getAddress().getHostAddress();
        Database database = mAuth.getInstance().getDatabase();


        Bukkit.getLogger().log(Level.SEVERE, "Verifying connection... ");

        if (!database.isSet(uuid)) {

            database.setPreviousIp(uuid, address);

            Bukkit.getLogger().log(Level.SEVERE, "Connect verified... [First Join]");
            return;
        }
        String lastAddress = database.getPreviousIp(uuid);
        if (lastAddress.equalsIgnoreCase("disabled")) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&',"&cYour account has been disabled for suspecious activity \nplease contact an administrator" ));
            return;
        }

        if (!lastAddress.equalsIgnoreCase(address)) {
            Bukkit.getLogger().log(Level.SEVERE, "Connect unverified [Location Changed]");
            mAuth.getInstance().getMAuthBot().getMessageHandler().sendMessage(new AuthFailedData(uuid, lastAddress, address, e.getName()));
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have connected from a different location \nPlease Contact an administrator."));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }


    }

}
