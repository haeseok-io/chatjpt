package org.discord.bot.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Papago {
    private static Dotenv dotenv = Dotenv.load();
    private static final String NAVER_CLIENT_ID = dotenv.get("NAVER_CLIENT_ID");
    private static final String NAVER_CLIENT_SEC = dotenv.get("NAVER_CLIENT_SECRET");

    // 언어 감지
    public static String requestDetect(String text) {
        String requestUrl = "https://openapi.naver.com/v1/papago/detectLangs";

        // 요청 헤더
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestHeader.put("X-Naver-Client-Id", NAVER_CLIENT_ID);
        requestHeader.put("X-Naver-Client-Secret", NAVER_CLIENT_SEC);

        return RequestAPI.post(requestUrl, requestHeader, "query="+text);
    }

    public static String requestTrans(String source, String target, String text) {
        String requestUrl = "https://openapi.naver.com/v1/papago/n2mt";

        // 요청 헤더
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestHeader.put("X-Naver-Client-Id", NAVER_CLIENT_ID);
        requestHeader.put("X-Naver-Client-Secret", NAVER_CLIENT_SEC);

        return RequestAPI.post(requestUrl, requestHeader, "source="+source+"&target="+target+"&text="+text);
    }

    // Map 형태로 변경
    public static Map<String, Object> convertData(String data) throws IOException {
        return JsonConvert.jsonToMap(data);
    }

}
