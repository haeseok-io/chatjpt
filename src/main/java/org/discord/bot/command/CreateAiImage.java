package org.discord.bot.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discord.bot.dto.RequestKarloDTO;
import org.discord.bot.util.JsonConvert;
import org.discord.bot.util.Papago;
import org.discord.bot.util.RequestAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAiImage implements CommandAction {
    private static Dotenv dotenv = Dotenv.load();
    private static final String COMMAND_NAME = "이미지생성";
    private static final String KAKAO_REST_API = dotenv.get("KAKAO_REST_API");

    @Override
    public String getDescription() {
        return "입력한 문장을 AI가 이미지로 그려줍니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping inputOption = event.getOption("입력");

        event.deferReply().queue(hook -> {
            hook.sendMessage("🤖 이미지 생성중...").queue(message -> {
                try {
                    String inputText = "";

                    // 입력한 언어 확인
                    String detectResponse = Papago.requestDetect(inputOption.getAsString());
                    Map<String, Object> detectData = JsonConvert.jsonToMap(detectResponse);

                    // 입력한 언어 영어로 변환
                    if( detectData.get("langCode").equals("en") ){
                        inputText = inputOption.getAsString();
                    } else {
                        String transResponse = Papago.requestTrans(detectData.get("langCode").toString(), "en", inputOption.getAsString());
                        Map<String, Object> transData = JsonConvert.jsonToMap(transResponse);

                        Map<String, Object> messageData = (Map<String, Object>) transData.get("message");
                        Map<String, Object> languageData = (Map<String, Object>) messageData.get("result");

                        inputText = languageData.get("translatedText").toString();
                    }

                    // 카카오 이미지 생성 요청
                    String requestUrl = "https://api.kakaobrain.com/v2/inference/karlo/t2i";
                    Map<String, String> requestHeader = new HashMap<>();
                    requestHeader.put("Authorization", "KakaoAK "+KAKAO_REST_API);
                    requestHeader.put("Content-Type", "application/json");

                    // 요청 정보
                    ObjectMapper mapper = new ObjectMapper();
                    RequestKarloDTO requestData = new RequestKarloDTO(inputText);

                    String requestJson = mapper.writeValueAsString(requestData);

                    String response = RequestAPI.post(requestUrl, requestHeader, requestJson);
                    Map<String, Object> responseData = JsonConvert.jsonToMap(response);

                    List<Map<String, Object>> imageList = (List<Map<String, Object>>) responseData.get("images");
                    Map<String, Object> imageData = (Map<String, Object>) imageList.get(0);

                    // 전송
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("🖼️ "+inputOption.getAsString());
                    embed.setImage(imageData.get("image").toString());


                    message.editMessage("✅ 이미지 생성 완료").queue();
                    message.editMessageEmbeds(embed.build()).queue();
                } catch(Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        OptionData inputOption = new OptionData(OptionType.STRING, "입력", "AI에게 그리고 싶은 그림을 설명해주세요.").setRequired(true);

        commandData.addOptions(inputOption);
        return commandData;
    }
}
