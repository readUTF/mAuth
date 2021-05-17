package com.readutf.mauth.profile;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.utils.SpigotUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public  class Profile {

    UUID uuid;
    String ip;
    String discordId;
    boolean deactivated;
    boolean verifyAddress;
    boolean tfa;

    public Profile(UUID uuid, String ip, String discordId, boolean deactivated, boolean verifyAddress, boolean tfa) {
        this.uuid = uuid;
        this.ip = ip;
        this.discordId = discordId;
        this.deactivated = deactivated;
        this.verifyAddress = verifyAddress;
        this.tfa = tfa;
    }

    public void setDeactivated(boolean deactivated) {
        System.out.println(deactivated);
        this.deactivated = deactivated;
        if(deactivated) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                Bukkit.getScheduler().runTask(mAuth.getInstance(), () -> {
                    player.kickPlayer(SpigotUtils.color("&cYour account has been disabled for suspecious activity \nplease contact an administrator"));
                });
                return;
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        mAuth.getInstance().getProfileDatabase().saveProfile(this);
    }

}
