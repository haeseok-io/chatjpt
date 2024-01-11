package org.discord.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface CommandAction {
    public String getDescription();
    public void execute(SlashCommandInteractionEvent event);
    public SlashCommandData build();
}
