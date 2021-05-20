package com.readutf.mauth.bot.utils;

import com.readutf.mauth.mAuth;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DiscordClickableHandler extends ListenerAdapter {

    @Getter private static List<DiscordClickable> discordClickables= new ArrayList<>();

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(event.getUser() == mAuth.getInstance().getMAuthBot().getJda().getSelfUser()) return;

        discordClickables.stream().filter(discordClickable -> event.getReaction().getReactionEmote().getName().equalsIgnoreCase(discordClickable.getEmote())).forEach(discordClickable -> {
            discordClickable.getRunnable().run();
            event.getReaction().removeReaction().queue();
            Message message = discordClickable.getMessage();

            if(message.getEmbeds().isEmpty()) {
                message.editMessage(message.getContentRaw()).queue(message1 -> {
                    message1.getReactions().forEach(messageReaction -> {
                        message1.removeReaction(messageReaction.getReactionEmote().getName()).queue();
                    });
                });
            } else {
                MessageEmbed messageEmbed = message.getEmbeds().get(0);
                message.editMessage(messageEmbed).queue(message1 -> {
                    message1.getReactions().forEach(messageReaction -> {
                        message1.removeReaction(messageReaction.getReactionEmote().getName()).queue();
                    });
                });
            }

        });


    }
}
