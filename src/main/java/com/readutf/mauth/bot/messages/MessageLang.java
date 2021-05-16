package com.readutf.mauth.bot.messages;

import com.readutf.mauth.ipdata.GeoLocationApi;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.UUID;

public class MessageLang {

    public static MessageEmbed getAuthFailedMessage(UUID uuid, String username, String oldIp, String newIp) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.YELLOW);

        embedBuilder.setTitle("**Authentication Failed**");
        embedBuilder.setDescription(username + " attempted to connect from an unknown location.");
        embedBuilder.addField("New Location", GeoLocationApi.getCountry(newIp), true);
        embedBuilder.addField("Old Location", GeoLocationApi.getCountry(oldIp), true);
        embedBuilder.setFooter("Click ✅ to verify connection or ❌ to disable account");
        embedBuilder.setThumbnail("https://crafatar.com/avatars/" + uuid.toString());
        return embedBuilder.build();
    }

    public static MessageEmbed getAuthAllowedMessage(String username, String user) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle("Connection Verified");
        embedBuilder.setDescription(username + "'s connection has been verified by " + user);
        return embedBuilder.build();
    }

    public static MessageEmbed getAccountDisabled(String username, String user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("Account Disabled");
        embedBuilder.setDescription(username + "'s account has been disabled by '" + user);
        return embedBuilder.build();
    }

}
