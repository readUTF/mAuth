package com.readutf.mauth.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.readutf.mauth.commands.Toggle2FACommand;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();


        Player player = e.getPlayer();
        System.out.println("1");
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());
        if (item == null || item.getType() != Material.FILLED_MAP || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            System.out.println("2");
            return;
        }



        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotUtils.color("&aPlease Scan."))) {

            System.out.println("3");
            e.getItemDrop().remove();
            profile.setTfa(true);
            player.getInventory().setItem(0, Toggle2FACommand.getSavedItem().get(player));
        }


    }

}
