package com.readutf.mauth.profile;

import com.readutf.mauth.mAuth;
import com.readutf.mauth.utils.SecureKeyGenerator;
import com.readutf.mauth.utils.SpigotUtils;
import de.taimos.totp.TOTP;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public  class Profile {

    UUID uuid;
    String ip;
    String discordId;
    String gAuthKey;
    boolean deactivated;
    boolean verifyAddress;
    boolean tfa;

    boolean authed;
    long lastAuth = 0;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.deactivated = false;
        this.verifyAddress = false;
        this.tfa = false;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
        if(deactivated) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                Bukkit.getScheduler().runTask(mAuth.getInstance(), () -> {
                    player.kickPlayer(SpigotUtils.color("&cYour account has been disabled for suspicious activity \nplease contact an administrator"));
                });
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        mAuth.getInstance().getProfileDatabase().saveProfile(this);
    }

    public void generateGAuthKey() {
        this.gAuthKey = SecureKeyGenerator.generateSecretKey();
    }

    public String getGAuthKey() {
        System.out.println(gAuthKey);
        if(gAuthKey == null)  {
            generateGAuthKey();
        }
        return gAuthKey;
    }

    public String getTOTPCode() {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(getGAuthKey());
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

}
