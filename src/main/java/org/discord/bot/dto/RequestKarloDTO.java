package org.discord.bot.dto;

public class RequestKarloDTO {
    private String prompt;

    public RequestKarloDTO(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
