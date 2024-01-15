package org.discord.bot.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Papago {
    private static Dotenv dotenv = Dotenv.load();
    private static final String NAVER_CLIENT_ID = dotenv.get("NAVER_CLIENT_ID");
    private static final String NAVER_CLIENT_SEC = dotenv.get("NAVER_CLIENT_SECRET");

    public static final Map<String, String> LANGUAGE = new HashMap<String, String>() {
        {
            put("ko", "한국어");
            put("en", "영어");
            put("ja", "일본어");
            put("zh-CN", "중국어 간체");
            put("zh-TW", "중국어 번체");
            put("vi", "베트남어");
            put("id", "인도네시아어");
            put("th", "태국어");
            put("de", "독일어");
            put("ru", "러시아어");
            put("es", "스페인어");
            put("it", "이탈리아어");
            put("fr", "프랑스어");
        }
    };

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

    public static Map<String, Object> requestDetectTrans(String target, String text) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("outLanguage", target);

        // 언어 감지
        String detectResponse = Papago.requestDetect(text);
        Map<String, Object> detectData = JsonConvert.jsonToMap(detectResponse);

        String detectLanguage = detectData.get("langCode").toString();
        result.put("inLanguage", detectLanguage);

        // 번역
        // - 감지된 언어와 번역하려는 언어가 같을 경우
        if( detectLanguage.equals(target) ) {
            result.put("transText", text);
            return result;
        }

        String transResponse = Papago.requestTrans(detectLanguage, target, text);
        Map<String, Object> transData = JsonConvert.jsonToMap(transResponse);
        Map<String, Object> messageData = (Map<String, Object>) transData.get("message");
        Map<String, Object> resultData = (Map<String, Object>) messageData.get("result");
        result.put("transText", resultData.get("translatedText").toString());

        return result;
    }

}
