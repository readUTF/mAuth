package com.readutf.mauth.bot.messages;

import com.cryptomorin.xseries.XSound;
import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.mAuthBot;
import com.readutf.mauth.bot.utils.DiscordClickable;
import com.readutf.mauth.bot.utils.DiscordClickableHandler;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class MessageHandler {

    @Getter
    private static HashMap<Message, AuthFailedData> authFailedData = new HashMap<>();

    @Getter
    public static HashMap<Message, Profile> tfaData = new HashMap<>();


    public static void send2FA(Profile profile) {
        Player player = profile.getPlayer();

        User user = mAuthBot.getInstance().getJda().retrieveUserById(profile.getDiscordId()).complete();
        if (user == null) {
            player.kickPlayer("Your account sync has broken, please contact an administrator.");
            return;
        }

        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(MessageLang.getVerificationMessage()).queue(message -> {
                new DiscordClickable(message, "✅", new BukkitRunnable() {
                    @Override
                    public void run() {
                        profile.setAuthed(true);
                        XSound.ENTITY_ARROW_HIT_PLAYER.play(player, 5, 1);
                        player.sendMessage(SpigotUtils.color("&aYou're login attempt has been verified."));
                        Bukkit.getScheduler().runTask(mAuth.getInstance(), () -> {
                            player.removePotionEffect(PotionEffectType.SLOW);
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                        });
                    }
                });
            });
        });


    }

    public static void sendLocationChangeMessage(AuthFailedData data) {
        if (authFailedData.values().stream().anyMatch(parcel1 -> parcel1.getUuid().equals(data.getUuid())))
            return;


        mAuthBot
                .getInstance()
                .getJda()
                .getGuilds()
                .forEach(guild -> {

                    List<TextChannel> textChannelsByName = guild.getTextChannelsByName("staff-activity", true);
                    if (textChannelsByName.isEmpty() || textChannelsByName.get(0) == null) return;
                    TextChannel textChannel = textChannelsByName.get(0);
                    textChannel.sendMessage(MessageLang.getAuthFailedMessage(data.getUuid(), data.getUsername(), data.getOldIp(), data.getNewIp())).queue(message -> {
                        authFailedData.put(message, data);
                        new DiscordClickable(message, "✅", () -> {
                            Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(data.getUuid());
                            profile.setIp(data.getNewIp());
                            profile.save();
                        });
                        new DiscordClickable(message, "❌", () -> {
                            Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(data.getUuid());
                            profile.setIp(data.getNewIp());
                            profile.setDeactivated(true);
                            profile.save();
                        });
                    });

                });
    }

}
