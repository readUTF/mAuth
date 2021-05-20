package com.readutf.mauth.listener;

import com.cryptomorin.xseries.XSound;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());


        if(profile.isTfa() && !profile.isAuthed()) {
            e.setCancelled(true);
            if(!mAuth.getInstance().getConfig().getString("2fa.type").equalsIgnoreCase("discord")) {
                System.out.println(profile.getTOTPCode());
                if(profile.getTOTPCode().equalsIgnoreCase(e.getMessage())) {
                    profile.setAuthed(true);
                    player.sendMessage(SpigotUtils.color("&aAuthenticated."));
                    XSound.ENTITY_ARROW_HIT_PLAYER.play(player, 5, 1);
                } else {
                    player.sendMessage(SpigotUtils.color("&cInvalid Code."));
                }
            }


        }

    }

}
