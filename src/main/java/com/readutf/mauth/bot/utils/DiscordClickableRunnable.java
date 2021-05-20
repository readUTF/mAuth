package com.readutf.mauth.bot.utils;

import net.dv8tion.jda.api.entities.Message;

public abstract class DiscordClickableRunnable implements Runnable{

    Message message;

    public DiscordClickableRunnable(Message message) {
        this.message = message;
    }

    public abstract void onClick();

    @Override
    public void run() {



        onClick();
    }
}
