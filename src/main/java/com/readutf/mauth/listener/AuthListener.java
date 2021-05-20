package com.readutf.mauth.listener;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AuthListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());
        if(profile.isTfa() && !profile.isAuthed()) {
            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                player.teleport(e.getFrom());
            }
        }


    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());

        if(profile.isTfa() && !profile.isAuthed()) {
            e.setCancelled(true);
            player.sendMessage(SpigotUtils.color("&cYou need to authenticate before running more commands."));
            return;
        }


    }

}
