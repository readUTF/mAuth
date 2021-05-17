package com.readutf.mauth.commands;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Toggle2FACommand implements CommandExecutor {

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
        profile.setTfa(!profile.isTfa());
        if(profile.isTfa()) {
            player.sendMessage(SpigotUtils.color("&aYou have enabled 2 factor authentication, you will receive your code when you join."));
            return true;
        } else {
            player.sendMessage(SpigotUtils.color("&cYou have disabled 2 factor authentication."));
            return true;
        }

    }
}
