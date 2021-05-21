package com.readutf.mauth.listener;

import com.readutf.mauth.commands.Toggle2FACommand;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMove implements Listener {

    @EventHandler
    public void itemMove(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        e.setCancelled(true);

        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());

        if (item == null || item.getType() != Material.FILLED_MAP || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotUtils.color("&aPlease Scan."))) {
            e.setCursor(null);
//            e.setCurrentItem(null);
            player.updateInventory();
        }

    }

}
