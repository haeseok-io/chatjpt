package org.discord.bot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discord.bot.util.JsonConvert;
import org.discord.bot.util.Papago;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PapagoTranslate implements CommandAction {
    private static final String COMMAND_NAME = "번역";

    @Override
    public String getDescription() {
        return "입력한 내용을 번역합니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping choiceOption = event.getOption("번역언어");
        OptionMapping inputOption = event.getOption("번역내용");

        try {
            String detectResponse = Papago.requestDetect(inputOption.getAsString());
            Map<String, Object> detectData = JsonConvert.jsonToMap(detectResponse);

            String transResponse = Papago.requestTrans(detectData.get("langCode").toString(), choiceOption.getAsString(), inputOption.getAsString());
            Map<String, Object> transData = JsonConvert.jsonToMap(transResponse);

            Map<String, Object> messageData = (Map<String, Object>) transData.get("message");
            Map<String, Object> resultData = (Map<String, Object>) messageData.get("result");


            // 문자열 가공
            String inputLang = Papago.LANGUAGE.get(resultData.get("srcLangType"));
            String outputLang = Papago.LANGUAGE.get(resultData.get("tarLangType"));
            String inputText = inputOption.getAsString();
            String outputText = resultData.get("translatedText").toString();

            // 전송
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle("번역 결과");
            embed.setDescription("요청해주신 내용을 감지하여 번역된 결과 입니다.");
            embed.setColor(0x00ff00);

            embed.addField("⬅️ 요청내용 ("+inputLang+")", inputText, true);
            embed.addField("➡️ 번역내용 ("+outputLang+")", outputText, false);
            
            event.replyEmbeds(embed.build()).setEphemeral(false).queue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        OptionData choiceOption = new OptionData(OptionType.STRING, "번역언어", "번역할 언어를 선택해주세요.").setRequired(true);
        OptionData inputOption = new OptionData(OptionType.STRING, "번역내용", "번역할 내용을 입력해주세요.").setRequired(true);

        choiceOption.addChoice("한국어", "ko");
        choiceOption.addChoice("영어", "en");

        commandData.addOptions(choiceOption, inputOption);
        return commandData;
    }
}
