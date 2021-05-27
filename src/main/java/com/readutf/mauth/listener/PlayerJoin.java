package com.readutf.mauth.listener;

import com.cryptomorin.xseries.XPotion;
import com.readutf.mauth.bot.authfailed.AuthFailedData;
import com.readutf.mauth.bot.messages.MessageHandler;
import com.readutf.mauth.bot.messages.MessageLang;
import com.readutf.mauth.mAuth;
import com.readutf.mauth.profile.Profile;
import com.readutf.mauth.utils.SpigotUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {


        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(e.getUniqueId());

        if(!profile.isVerifyAddress() && !mAuth.getInstance().isUseDiscord()) return;


        if(profile.isDeactivated()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&',"&cYour account has been disabled for suspecious activity \nplease contact an administrator" ));
            return;
        }
        String address = e.getAddress().getHostName();


        if(profile.getIp() == null) {
            profile.setIp(address);
            mAuth.getInstance().getProfileDatabase().saveProfile(profile);
            return;
        }
        if(!profile.getIp().equalsIgnoreCase(address)) {
            Bukkit.getLogger().log(Level.SEVERE, "Connect unverified [Location Changed]");
            MessageHandler.sendLocationChangeMessage(new AuthFailedData(e.getUniqueId(), profile.getIp(), address, e.getName()));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have connected from a different location \nPlease Contact an administrator."));
        }


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Profile profile = mAuth.getInstance().getProfileDatabase().getProfile(e.getPlayer().getUniqueId());
        e.setJoinMessage(null);

        long start = System.currentTimeMillis();

        if(profile.isVerifyAddress()) {
            player.sendMessage(SpigotUtils.color("&a[mAuth] Your connection has been verified."));
        }

        if(profile.isTfa()) {
            if(System.currentTimeMillis() - profile.getLastAuth() > TimeUnit.HOURS.toMillis(2)) {
                if(mAuth.getInstance().getConfig().getString("2fa.type").equalsIgnoreCase("discord")) {

                    if(profile.getDiscordId() == null) {
                        player.sendMessage(SpigotUtils.color("&cYou need to re-sync your discord account."));
                        return;
                    }

                    player.sendMessage(SpigotUtils.color("&cYour previous session expired, please check discord to authenticate."));
                    player.addPotionEffect(XPotion.SLOW.parsePotion(Integer.MAX_VALUE, 255));
                    player.addPotionEffect(XPotion.BLINDNESS.parsePotion(Integer.MAX_VALUE, 255));

                    MessageHandler.send2FA(profile);

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            if(profile.isAuthed()) {this.cancel(); return;}

                            int timeUntill = 20 - (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start);

                            if(timeUntill < 1) {
                                player.kickPlayer(SpigotUtils.color("You did not authenticate in time."));
                                MessageHandler.getTfaData().entrySet().stream().filter(entry -> entry.getValue() == profile).forEach(messageProfileEntry -> {
                                    messageProfileEntry.getKey().editMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Auth Failed").setDescription("You failed to authenticate in time.").build()).queue(message -> {
                                        message.getReactions().forEach(messageReaction -> message.removeReaction(messageReaction.getReactionEmote().getName()).queue());
                                    });
                                    this.cancel();
                                });
                                return;
                            }

                            player.sendTitle(SpigotUtils.color("&c&lPlease Verify"), SpigotUtils.color("&fYou will be kicked in " + timeUntill + " seconds."), 0, 10, 0);

                        }
                    }.runTaskTimer(mAuth.getInstance(), 0, 5);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            player.sendMessage(SpigotUtils.color("&cYour previous session expired, please check your google authenticator and enter the code in chat."));
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if(profile.isAuthed() || (player == null || player.isOnline())) {this.cancel(); return;}

                                    int timeUntill = 20 - (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start);

                                    if(timeUntill < 1) {
                                        player.kickPlayer(SpigotUtils.color("You did not authenticate in time."));
                                        MessageHandler.getTfaData().entrySet().stream().filter(entry -> entry.getValue() == profile).forEach(messageProfileEntry -> {
                                            messageProfileEntry.getKey().editMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Auth Failed").setDescription("You failed to authenticate in time.").build()).queue(message -> {
                                                message.getReactions().forEach(messageReaction -> message.removeReaction(messageReaction.getReactionEmote().getName()).queue());
                                            });
                                            this.cancel();
                                        });
                                        return;
                                    }

                                    player.sendTitle(SpigotUtils.color("&c&lPlease Verify"), SpigotUtils.color("&fYou will be kicked in " + timeUntill + " seconds."), 0, 10, 0);

                                }
                            }.runTaskTimer(mAuth.getInstance(), 0, 5);
                        }
                    }.runTaskLater(mAuth.getInstance(), 1);
                }

            } else {
                profile.setLastAuth(System.currentTimeMillis());
            }
        }



    }

}
