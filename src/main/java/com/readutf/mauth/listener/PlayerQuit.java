package com.readutf.mauth.listener;

import com.cryptomorin.xseries.XMaterial;
import com.readutf.mauth.commands.Toggle2FACommand;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.ProfileDatabase;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        ProfileDatabase profileDatabase = mAuth.getInstance().getProfileDatabase();

        profileDatabase.saveProfile(profileDatabase.getProfile(player.getUniqueId()));

        ItemStack item = player.getInventory().getItem(0);

        if(item != null && item.getType() == XMaterial.FILLED_MAP.parseMaterial() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotUtils.color("&f&l&l&rPlease Scan."))) {
            if(Toggle2FACommand.getSavedItem().containsKey(e.getPlayer())) {
                e.getPlayer().getInventory().setItem(0, Toggle2FACommand.getSavedItem().get(e.getPlayer()));
            }
        }
    }

}
