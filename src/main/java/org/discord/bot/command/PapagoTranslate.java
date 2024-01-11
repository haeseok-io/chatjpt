package org.discord.bot.command;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discord.bot.util.RequestAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PapagoTranslate implements CommandAction {
    private static Dotenv dotenv = Dotenv.load();
    private static final String COMMAND_NAME = "파파고번역";
    private static final String NAVER_CLIENT_ID = dotenv.get("NAVER_CLIENT_ID");
    private static final String NAVER_CLIENT_SEC = dotenv.get("NAVER_CLIENT_SECRET");

    @Override
    public String getDescription() {
        return "네이버 파파고 번역";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping choiceOption = event.getOption("번역언어");
        OptionMapping inputOption = event.getOption("번역내용");

        System.out.println(NAVER_CLIENT_ID);
        System.out.println(NAVER_CLIENT_SEC);

        // 언어
        String query;
        try {
            query = URLEncoder.encode("query="+inputOption.getAsString(), "UTF-8");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("error", e);
        }

        // 언어감지 API
        String detectUrl = "https://openapi.naver.com/v1/papago/detectLangs";
        Map<String, String> detectHeader = new HashMap<>();
        detectHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        detectHeader.put("X-Naver-Client-Id", NAVER_CLIENT_ID);
        detectHeader.put("X-Naver-Client-Secret", NAVER_CLIENT_SEC);

        String responseBody = RequestAPI.post(detectUrl, detectHeader, query);
        System.out.println(responseBody);

        // 전송
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("파파고 번역 결과");
        event.replyEmbeds(embed.build()).setEphemeral(false).queue();
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        OptionData choiceOption = new OptionData(OptionType.STRING, "번역언어", "번역할 언어를 선택해주세요.").setRequired(true);
        OptionData inputOption = new OptionData(OptionType.STRING, "번역내용", "번역할 내용을 입력해주세요.").setRequired(true);

        choiceOption.addChoice("한국어", "한국어");
        choiceOption.addChoice("영어", "영어");

        commandData.addOptions(choiceOption, inputOption);
        return commandData;
    }
}
