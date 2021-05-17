package com.readutf.mauth.bot.listeners;

import com.cryptomorin.xseries.XSound;
import com.readutf.mauth.commands.SyncCommand;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class SyncMessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        if(!e.getChannel().getName().equalsIgnoreCase("sync-account")) {
            return;
        }

        e.getMessage().delete().queue();
        String code = e.getMessage().getContentRaw();

        if(SyncCommand.getSyncKeys().keySet().stream().anyMatch(s -> s.equalsIgnoreCase(code))) {
            Profile profile = SyncCommand.getSyncKeys().get(code);
            profile.setDiscordId(e.getMessage().getAuthor().getId());
            profile.save();
            Player player = profile.getPlayer();
            if(player != null) {
                player.sendMessage(SpigotUtils.color("&aYour account was synced successfully."));
                XSound.ENTITY_ARROW_HIT_PLAYER.play(player);


            }
        }


    }
}
