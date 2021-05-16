package com.readutf.mauth.bot.commands;


import com.readutf.mauth.mAuth;
import com.readutf.mauth.utils.UUIDCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiscordCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if(!message.startsWith("!")) {
            return;
        }

        message = message.substring(1);
        String[] args = message.split(" ");

        if(args[0].equalsIgnoreCase("reactivate")) {
            event.getMessage().delete().queue();
            if(args.length < 2) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Usage: /reactivate <username>").build()).queue();
                return;
            }

            String username = args[1];
            UUID uuid = UUIDCache.getUUID(username);
            if(uuid == null) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Invalid Username").build()).queue();
                return;
            }

            if(!mAuth.getInstance().getDatabase().getPreviousIp(uuid).equalsIgnoreCase("disabled")) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Account is not deactivated").build()).queue();
                return;
            }
            mAuth.getInstance().getDatabase().removeIp(uuid);
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Account Reactivated").setDescription(username + "'s account has been reactivated.").build()).queue();
        } else if(args[0].equalsIgnoreCase("deactivate")) {

            event.getMessage().delete().queue();
            if(args.length < 2) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Usage: /deactivate <username>").build()).queue();
                return;
            }

            String username = args[1];
            UUID uuid = UUIDCache.getUUID(username);
            if(uuid == null) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Invalid Username").build()).queue();
                return;
            }

            if(mAuth.getInstance().getDatabase().getPreviousIp(uuid).equalsIgnoreCase("disabled")) {
                event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Account already deactivated").build()).queue();
                return;
            }

            mAuth.getInstance().getDatabase().setPreviousIp(uuid, "disabled");
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Account Deactivated").setDescription(username + "'s account has been deactivated.").build()).queue();
        }


    }

}
