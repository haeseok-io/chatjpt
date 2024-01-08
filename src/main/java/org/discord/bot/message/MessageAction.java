package org.discord.bot.message;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MessageAction {
    public void execute(MessageReceivedEvent event);
}
