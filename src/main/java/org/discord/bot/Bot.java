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
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discord.bot.command.CommandAction;
import org.discord.bot.command.PapagoTranslate;
import org.discord.bot.command.Ping;
import org.discord.bot.command.RandomDogImage;
import org.discord.bot.message.MessageAction;

import java.io.IOException;
import java.util.ArrayList;
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
                new PapagoTranslate().build()
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
            case PREFIX+"ping" : action = new org.discord.bot.message.Ping(); break;
        }

        // 실행
        action.execute(event);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandAction action = null;

        switch (event.getName()){
            case "ping" : action = new org.discord.bot.command.Ping(); break;
            case "랜덤강아지" : action = new RandomDogImage(); break;
            case "파파고번역" : action = new PapagoTranslate(); break;
        }

        // 커맨드 실행
        action.execute(event);
    }
}
