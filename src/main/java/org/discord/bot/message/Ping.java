package org.discord.bot.message;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements MessageAction {
    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannelUnion channel = event.getChannel();
        channel.sendMessage("Pong!").queue();
    }
}
