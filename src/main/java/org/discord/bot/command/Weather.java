package org.discord.bot.command;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.InteractionHookImpl;
import org.discord.bot.util.WeatherData;

public class Weather implements CommandAction {
    private static Dotenv dotenv = Dotenv.load();
    private static final String COMMAND_NAME = "날씨";
    private static final String API_KEY = dotenv.get("WEATHER_KEY");
    private WeatherData weatherData = new WeatherData();

    @Override
    public String getDescription() {
        return "선택하신 지역의 날씨정보를 확인합니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        OptionMapping si = event.getOption("시");

        // 전송
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("날씨 결과");
        event.getHook().editOriginal("Command execute!").queue();
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        OptionData siOption = new OptionData(OptionType.STRING, "시", "시를 선택해주세요.", true);

        for(String si : weatherData.getWeatherSi()) {
            System.out.println(si);
            siOption.addChoice(si, si);
        }

        commandData.addOptions(siOption);
        return commandData;
    }
}
