package com.readutf.mauth.commands;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import com.readutf.mauth.utils.UUIDCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReActivateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("mauth.command.reactivate")) {
            sender.sendMessage(SpigotUtils.color("&cInvalid Permission"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(SpigotUtils.color("&cUsage: /reactivate <player>"));
            return true;
        }

        if(args.length == 1) {
            UUID uuid = UUIDCache.getUUID(args[0]);
            if(uuid == null) {
                sender.sendMessage(SpigotUtils.color("Invalid UUID"));
                return true;
            }
            Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(uuid);
            profile.setDeactivated(false);
            profile.save();
            return true;
        }
        return true;
    }
}
