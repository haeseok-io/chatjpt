package org.discord.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Ping implements CommandAction {
    public static final String COMMAND_NAME = "ping";

    @Override
    public String getDescription() {
        return "서버와의 응답시간을 반환합니다.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();

        // setEphemeral : 상호작용을 트리거한 사용자에게만 표시
        // flatMap : event.reply 응답에 대한 처리가 완료되면 실행되는 코드
        // getHook : 메시지와 상호 작용하고 수정 가능
        // editOriginalFormat : 특정 상호작용에 대해 봇이 보낸 원래 응답을 수정하는 데 사용
        event.reply("응답시간")
            .setEphemeral(true)
            .flatMap(v -> event.getHook().editOriginalFormat("응답시간 : %d ms", System.currentTimeMillis() - time))
            .queue();
    }

    @Override
    public SlashCommandData build() {
        SlashCommandData commandData = Commands.slash(COMMAND_NAME, getDescription());
        return commandData;
    }
}
