package org.discord.bot.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonConvert {
    private static ObjectMapper mapper = new ObjectMapper();
    public static Map<String, Object> jsonToMap(String jsonData) throws IOException {
        Map<String, Object> map = mapper.readValue(jsonData, Map.class);
        return map;
    }
}
