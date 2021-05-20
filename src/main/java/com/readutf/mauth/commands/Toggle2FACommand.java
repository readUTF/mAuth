package com.readutf.mauth.commands;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

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
        if(mAuth.getInstance().getConfig().getString("2fa.type").equalsIgnoreCase("discord")) {
            if(profile.getDiscordId() != null) {
                player.sendMessage(SpigotUtils.color("You have not linked your discord yet, please use /sync"));

                return true;
            }
            profile.setTfa(!profile.isTfa());
            if(profile.isTfa()) {
                player.sendMessage(SpigotUtils.color("&aYou have enabled 2 factor authentication, you will receive your code when you join."));
                return true;
            } else {
                player.sendMessage(SpigotUtils.color("&cYou have disabled 2 factor authentication."));
                return true;
            }
        } else {
            profile.save();
            String key = profile.getGAuthKey();

            if(args.length == 0) {
                player.sendMessage(key);
            } else {

                System.out.println(profile.getTOTPCode());

                if(profile.getTOTPCode().equalsIgnoreCase(args[0])) {
                    player.sendMessage("it worked!!!");
                } else {
                    player.sendMessage("pain");
                }

            }

        }
        return true;
    }
}
