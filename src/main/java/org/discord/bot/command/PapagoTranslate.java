package org.discord.bot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discord.bot.util.Papago;

import java.io.IOException;
import java.util.Map;

public class PapagoTranslate implements CommandAction {
    private static final String COMMAND_NAME = "파파고번역";

    @Override
    public String getDescription() {
        return "네이버 파파고 번역";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping choiceOption = event.getOption("번역언어");
        OptionMapping inputOption = event.getOption("번역내용");

        try {
            String detectResponse = Papago.requestDetect(inputOption.getAsString());
            Map<String, Object> detectData = Papago.convertData(detectResponse);

            String transResponse = Papago.requestTrans(detectData.get("langCode").toString(), choiceOption.getAsString(), inputOption.getAsString());
            Map<String, Object> transData = Papago.convertData(transResponse);

            //Map<String, Object> resultData = Papago.convertData();
            System.out.println(transData.get("message").toString().replaceAll("\\{type=[^}]*, service=[^}]*, version=[^}]*, ", ""));





            // 전송
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("파파고 번역 결과");
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
