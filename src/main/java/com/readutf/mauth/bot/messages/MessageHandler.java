package com.readutf.mauth.bot.messages;

import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.mAuthBot;
import com.readutf.mauth.profile.Profile;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class MessageHandler {

    @Getter
    private static HashMap<Message, AuthFailedData> messages = new HashMap<>();


    public static void send2FA(Profile profile) {
        Player player = profile.getPlayer();

        User user = mAuthBot.getInstance().getJda().retrieveUserById(profile.getDiscordId()).complete();
        if (user == null) {
            player.kickPlayer("Your account sync has broken, please contact an administrator.");
            return;
        }
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(MessageLang.getVerificationMessage(player)).queue(message -> {
                message.addReaction("❌").queue();
                message.addReaction("✅").queue();
            });
        });


    }

    public static void sendLocationChangeMessage(AuthFailedData parcel) {
        if (messages.values().stream().anyMatch(parcel1 -> parcel1.getUuid().equals(parcel.getUuid()))) return;


        mAuthBot
                .getInstance()
                .getJda()
                .getGuilds()
                .forEach(guild -> {

                    List<TextChannel> textChannelsByName = guild.getTextChannelsByName("staff-activity", true);
                    if (textChannelsByName.isEmpty() || textChannelsByName.get(0) == null) return;
                    TextChannel textChannel = textChannelsByName.get(0);
                    textChannel.sendMessage(MessageLang.getAuthFailedMessage(parcel.getUuid(), parcel.getUsername(), parcel.getOldIp(), parcel.getNewIp())).queue(message -> {
                        messages.put(message, parcel);
                        message.addReaction("❌").queue();
                        message.addReaction("✅").queue();
                    });

                });
    }

}
