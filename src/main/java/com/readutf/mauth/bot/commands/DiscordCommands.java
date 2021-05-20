package com.readutf.mauth.bot.commands;


import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.UUIDCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.rmi.PortableRemoteObject;
import java.awt.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiscordCommands extends ListenerAdapter {

    
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getGuild().getTextChannelsByName("staff-activity", true).isEmpty()) return;
        if(!event.getGuild().getTextChannelsByName("staff-activity", true).contains(event.getChannel())) return;
        
        String message = event.getMessage().getContentRaw();

        if(!message.startsWith("!")) {
            return;
        }

        message = message.substring(1);
        String[] args = message.split(" ");

        if(args[0].equalsIgnoreCase("reactivate")) {
            event.getMessage().delete().queue();
            if(args.length < 2) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Usage: !reactivate <username>").build()).queue();
                return;
            }

            String username = args[1];
            UUID uuid = UUIDCache.getUUID(username);
            Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(uuid);
            if(uuid == null) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Invalid Username").build()).queue();
                return;
            }

            if(!profile.isDeactivated()) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Account is not deactivated").build()).queue();
                return;
            }
            profile.setDeactivated(false);
            profile.save();

            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Account Reactivated").setDescription(username + "'s account has been reactivated.").build()).queue();
        } else if(args[0].equalsIgnoreCase("deactivate")) {

            event.getMessage().delete().queue();
            if(args.length < 2) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Usage: !deactivate <username>").build()).queue();
                return;
            }

            String username = args[1];
            UUID uuid = UUIDCache.getUUID(username);
            Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(uuid);
            if(uuid == null) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Invalid Username").build()).queue();
                return;
            }

            if(profile.isDeactivated()) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Account already deactivated").build()).queue();
                return;
            }

            profile.setDeactivated(true);
            profile.save();
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Account Deactivated").setDescription(username + "'s account has been deactivated.").build()).queue();
        }


    }

}
