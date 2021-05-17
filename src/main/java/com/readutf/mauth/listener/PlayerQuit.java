package com.readutf.mauth.listener;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.ProfileDatabase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        ProfileDatabase profileDatabase = mAuth.getInstance().getProfileDatabase();

        profileDatabase.saveProfile(profileDatabase.getProfile(player.getUniqueId()));
    }

}
