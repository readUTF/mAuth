package com.readutf.mauth.bot.utils;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;

@Getter
public class DiscordClickable {

    private Message message;
    private String emote;
    private Runnable runnable;

    public DiscordClickable(Message message, String emote, Runnable runnable) {
        this.message = message;
        this.emote = emote;
        this.runnable = runnable;

        message.addReaction(emote).queue();
        DiscordClickableHandler.getDiscordClickables().add(this);
    }

}
