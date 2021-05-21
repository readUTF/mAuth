package com.readutf.mauth.commands;

import com.cryptomorin.xseries.XMaterial;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SecureKeyGenerator;
import com.readutf.mauth.utils.SpigotUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.stream.IntStream;

public class Toggle2FACommand implements CommandExecutor {

    @Getter private static HashMap<Player, ItemStack> savedItem = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cPlayer only."));
            return true;
        }

        if(!sender.hasPermission("mauth.command.pin")) {
            sender.sendMessage(SpigotUtils.color("&cNo Perm."));
            return true;
        }
        Player player = (Player) sender;


        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());
        if(mAuth.getInstance().getConfig().getString("2fa.type").equalsIgnoreCase("discord") && mAuth.getInstance().getConfig().getBoolean("bot.enabled")) {

            if(profile.getDiscordId() == null) {
                player.sendMessage(SpigotUtils.color("&cYou have not linked your discord yet, please use /sync"));
                return true;
            }
            profile.setTfa(!profile.isTfa());
            profile.save();
            if(profile.isTfa()) {
                player.sendMessage(SpigotUtils.color("&aYou have enabled 2 factor authentication, you will receive your code when you join."));
                return true;
            } else {
                player.sendMessage(SpigotUtils.color("&cYou have disabled 2 factor authentication."));
                return true;
            }
        } else {

            if(profile.getGAuthKey() != null && profile.isTfa()) {
                if(!(args.length > 0 && args[0].equalsIgnoreCase("reset"))) {
                    player.sendMessage(SpigotUtils.color("&aGoogle authenticator already setup! If you want to reset it please do /2fa reset"));
                    return true;
                }
            }

            player.sendMessage(SpigotUtils.color("&aPlease download the Google Authenticator, scan the barcode on the map in your inventory, and drop when done!"));

            MapView view = Bukkit.createMap(player.getWorld());
            view.addRenderer(new MapRenderer() {
                boolean ran = false;

                @Override
                public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
                    if(ran) return;
                    canvas.drawImage(0, 0, SecureKeyGenerator.createQRCode(
                            SecureKeyGenerator.getGoogleAuthenticatorBarCode(profile.getGAuthKey(), player.getName(), "mAuth"), 128, 128));
                    ran = true;
                }
            });
            player.getInventory().setHeldItemSlot(0);
            lookDown(player);
            ItemStack itemStack = XMaterial.FILLED_MAP.parseItem();
            MapMeta itemMeta = (MapMeta) itemStack.getItemMeta();
            itemMeta.setMapView(view);
            itemMeta.setDisplayName(SpigotUtils.color("&aPlease Scan."));
            itemStack.setItemMeta(itemMeta);

            savedItem.put(player, player.getInventory().getItem(0));
            player.getInventory().setItem(0, itemStack);
        }
        return true;
    }

    public void lookDown(Player player) {
        Location location = player.getLocation().clone();
        location.setPitch(90);
        player.teleport(location);
    }

}
