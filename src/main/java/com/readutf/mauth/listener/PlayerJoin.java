package com.readutf.mauth.listener;

import com.cryptomorin.xseries.XPotion;
import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.messages.MessageHandler;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {


        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(e.getUniqueId());

        if(!profile.isVerifyAddress()) return;


        if(profile.isDeactivated()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&',"&cYour account has been disabled for suspecious activity \nplease contact an administrator" ));
            return;
        }
        String address = e.getAddress().getHostName();


        if(profile.getIp() == null) {
            profile.setIp(address);
            mAuth.getInstance().getProfileDatabase().saveProfile(profile);
            return;
        }
        if(!profile.getIp().equalsIgnoreCase(address)) {
            Bukkit.getLogger().log(Level.SEVERE, "Connect unverified [Location Changed]");
            MessageHandler.sendLocationChangeMessage(new AuthFailedData(e.getUniqueId(), profile.getIp(), address, e.getName()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have connected from a different location \nPlease Contact an administrator."));
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(e.getPlayer().getUniqueId());

        if(profile.isTfa()) {
            player.sendMessage(SpigotUtils.color("&cYour previous session expired, please check discord to authenticate."));
            player.addPotionEffect(XPotion.SLOW.parsePotion(Integer.MAX_VALUE, 255));

            MessageHandler.send2FA(profile);
            return;
        }



    }

}
