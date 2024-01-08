package org.discord.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandAction {
    public String getDescription();
    public void execute(SlashCommandInteractionEvent event);
}
