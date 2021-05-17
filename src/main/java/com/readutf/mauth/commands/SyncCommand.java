package com.readutf.mauth.commands;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.management.remote.JMXAuthenticator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SyncCommand implements CommandExecutor {


    @Getter private static HashMap<String, Profile> syncKeys = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player."));
            return true;
        }
        Player player = (Player) sender;
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(player.getUniqueId());
        String key = "";
        if(!SyncCommand.getSyncKeys().values().contains(profile)) {
            key = new Random().nextInt(900000) + 100000 + "";
            while (SyncCommand.getSyncKeys().containsKey(key)) {
                key = new Random().nextInt(900000) + 100000 + "";
            }
        } else {
            for (Map.Entry<String, Profile> entry : SyncCommand.getSyncKeys().entrySet()) {
                String s = entry.getKey();
                Profile profile1 = entry.getValue();
                if (profile1 == profile) {
                    key = s;
                }
            }
        }

        String finalKey = key;
        mAuth.getInstance().getConfig().getStringList("sync.message").stream().map(SpigotUtils::color).forEach(s -> {

            if(s.contains("{code}")) {
                ComponentBuilder builder = new ComponentBuilder();
                int start = s.indexOf("{code}");
                int end = start + 6;

                builder.append(s.substring(0, start));
                builder.append(SpigotUtils.color("&a&n" + finalKey));
                builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, finalKey));
                builder.append(s.substring(end));
                player.spigot().sendMessage(builder.create());
            } else {
                player.sendMessage(SpigotUtils.color(s));
            }

        });

        syncKeys.put(key, profile);
        return true;
    }
}

