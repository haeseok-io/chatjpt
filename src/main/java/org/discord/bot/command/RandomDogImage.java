package org.discord.bot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.discord.bot.util.JsonConvert;
import org.discord.bot.util.RequestAPI;

import java.util.Map;

public class RandomDogImage implements CommandAction {
    @Override
    public String getDescription() {
        return "무작위 강아지 이미지를 가져옵니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        RequestAPI requestAPI = new RequestAPI();

        try {
            // API 요청
            String jsonData = requestAPI.requestGET("https://dog.ceo/api/breeds/image/random", null);
            Map<String, Object> data = JsonConvert.jsonToMap(jsonData);

            // Embed 전송
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage(data.get("message").toString());

            event.replyEmbeds(embed.build()).setEphemeral(false).queue();
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("데이터 처리중 오류 발생").setEphemeral(true).queue();
        }
    }
}
