package com.readutf.mauth.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ErrorJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(e.getPlayer().isOp() || e.getPlayer().hasPermission("mAuth.admin")) {
            e.getPlayer().sendMessage("An error occured whilst loading mAuth, please check the console.");
        }
    }

}
