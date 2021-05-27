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

public class VerifyAddressCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String c, @NotNull String[] args) {
        if(!sender.hasPermission("mauth.command.veryaddress")) {
            sender.sendMessage(SpigotUtils.color("&cNo Perm."));
            return true;
        }
        if(!mAuth.getInstance().isUseDiscord()) {
            sender.sendMessage(SpigotUtils.color("&cThis feature requires the discord bot to be enabled."));
            return true;
        }
        if(args.length < 1) {
            sender.sendMessage(SpigotUtils.color("&cUsage: /" + c + " <player>"));
            return true;
        }



        UUID uuid = UUIDCache.getUUID(args[0]);
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(uuid);
        boolean verifyAddress = !profile.isVerifyAddress();

        profile.setVerifyAddress(verifyAddress);
        if(verifyAddress) {
            sender.sendMessage(SpigotUtils.color("&a" + args[0] + "'s ip will now be verified on connection."));
        } else {
            sender.sendMessage(SpigotUtils.color("&c" + args[0] + "'s ip will no longer be verified on connection."));
        }

        profile.save();

        return true;
    }
}
