package com.readutf.mauth.bot.listeners;


import com.readutf.mauth.bot.messages.MessageHandler;
import com.readutf.mauth.bot.messages.MessageLang;
import com.readutf.mauth.mAuth;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        List<Message> remove = new ArrayList<>();

        AtomicBoolean ran = new AtomicBoolean(false);

        MessageHandler.getMessages().forEach((message, data) -> {
            if (!message.getId().equalsIgnoreCase(event.getMessageId())) return;
            if (ChronoUnit.MILLIS.between(message.getTimeCreated(), OffsetDateTime.now()) < 1500) return;


            if (event.getReaction().getReactionEmote().getName().equalsIgnoreCase("✅")) {
                event.getReaction().removeReaction().queue();
                remove.add(message);

                message.editMessage(MessageLang.getAuthAllowedMessage(data.getUsername(), "<@" + event.getUser().getId() + ">")).queue(message1 -> {
                    message1.getReactions().forEach(messageReaction -> {
                        message1.removeReaction(messageReaction.getReactionEmote().getName()).queue();
                    });
                });

                if (!ran.get()) {
                    mAuth.getInstance().getDatabase().setPreviousIp(data.getUuid(), data.getNewIp());
                    ran.set(true);
                }

            } else if (event.getReaction().getReactionEmote().getName().equalsIgnoreCase("❌")) {
                if (message.getId().equalsIgnoreCase(event.getMessageId())) {
                    event.getReaction().removeReaction().queue();
                    remove.add(message);

                    message.editMessage(MessageLang.getAccountDisabled(data.getUsername(), "<@" + event.getUser().getId() + ">")).queue(message1 -> {
                        message1.getReactions().forEach(messageReaction -> {
                            message1.removeReaction(messageReaction.getReactionEmote().getName()).queue();
                        });
                    });

                    if (!ran.get()) {
                        mAuth.getInstance().getDatabase().setPreviousIp(data.getUuid(), "disabled");
                        ran.set(true);
                    }
                }
            }
        });
        remove.forEach(message -> MessageHandler.getMessages().remove(message));
    }
}
