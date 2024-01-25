package org.discord.bot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discord.bot.command.*;
import org.discord.bot.message.MessageAction;

import java.io.IOException;
import java.util.EnumSet;

public class Bot extends ListenerAdapter {
    private static Dotenv dotenv = Dotenv.load();
    private static final String PREFIX = "jpt:";
    private static final String TOKEN = dotenv.get("DISCORD_CLIENT_TOKEN");

    public static void main(String[] args) throws IOException {
        // 사용할 이벤트 정의
        EnumSet<GatewayIntent> intents = EnumSet.of(
            // Enables MessageReceivedEvent for guild (also known as servers)
            GatewayIntent.GUILD_MESSAGES,
            // Enables the event for private channels (also known as direct messages)
            GatewayIntent.DIRECT_MESSAGES,
            // Enables access to message.getContentRaw()
            GatewayIntent.MESSAGE_CONTENT,
            // Enables MessageReactionAddEvent for guild
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            // Enables MessageReactionAddEvent for private channels
            GatewayIntent.DIRECT_MESSAGE_REACTIONS
        );

        // 봇 빌드
        JDA jda = JDABuilder.createLight(TOKEN, intents)
                .addEventListeners(new Bot())
                .setActivity(Activity.listening("/명령어 or jpt:명령어"))
                .build();

        // 슬래시 커멘드 정의
        jda.updateCommands().addCommands(
                new Ping().build(),
                new RandomDogImage().build(),
                new PapagoTranslate().build(),
                new CreateAiImage().build(),
                new Weather().build()
        ).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is Ready!!");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageAction action = null;
        Message message = event.getMessage();

        // 봇 자신의 메세지는 무시
        if( event.getAuthor().isBot() )     return;

        // 명령어
        switch ( message.getContentRaw() ) {
            case PREFIX+"ping" :
                action = new org.discord.bot.message.Ping();
                action.execute(event);
                break;
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandAction action = null;

        switch (event.getName()){
            case "ping" :
                action = new org.discord.bot.command.Ping();
                action.execute(event);
                break;
            case "랜덤강아지" :
                action = new RandomDogImage();
                action.execute(event);
                break;
            case "번역" :
                action = new PapagoTranslate();
                action.execute(event);
                break;
            case "이미지생성" :
                action = new CreateAiImage();
                action.execute(event);
                break;
            case "날씨" :
                action = new Weather();
                action.execute(event);
                break;
        }
    }
}
