package com.readutf.mauth.bot.messages;

import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.mAuthBot;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;

public class MessageHandler {

    @Getter
    private static HashMap<Message, AuthFailedData> messages = new HashMap<>();

    public static void sendMessage(AuthFailedData parcel) {
        mAuthBot
                .getInstance()
                .getJda()
                .getGuilds()
                .forEach(guild -> {


                    List<TextChannel> textChannelsByName = guild.getTextChannelsByName("staff-activity", true);
                    if(textChannelsByName.isEmpty() || textChannelsByName.get(0) == null) return;
                    TextChannel textChannel = textChannelsByName.get(0);
                    textChannel.sendMessage(MessageLang.getAuthFailedMessage(parcel.getUuid(), parcel.getUsername(), parcel.getOldIp(), parcel.getNewIp())).queue(message -> {
                        messages.put(message, parcel);
                        message.addReaction("❌").queue();
                        message.addReaction("✅").queue();
                    });

                });
    }

}
