package org.discord.bot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discord.bot.util.JsonConvert;
import org.discord.bot.util.RequestAPI;

import java.io.IOException;
import java.util.Map;

public class RandomDogImage implements CommandAction {
    public static final String COMMAND_NAME = "랜덤강아지";

    @Override
    public String getDescription() {
        return "무작위 강아지 이미지를 가져옵니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        try {
            // API 요청
            String jsonData = RequestAPI.get("https://dog.ceo/api/breeds/image/random", null, null);
            Map<String, Object> data = JsonConvert.jsonToMap(jsonData);


            System.out.println(data.get("message").toString());
            // Embed 전송
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage(data.get("message").toString());

            event.replyEmbeds(embed.build()).setEphemeral(false).queue();
        } catch (IOException e) {
            e.printStackTrace();
            event.reply("데이터 처리중 오류 발생").setEphemeral(true).queue();
        }
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        return commandData;
    }
}
