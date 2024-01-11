package org.discord.bot.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestAPI {

    private static HttpURLConnection connect(String url) {
        try {
            URL requestUrl = new URL(url);
            return (HttpURLConnection) requestUrl.openConnection();
        } catch(MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : "+url, e);
        } catch (IOException e) {
            throw new RuntimeException("연결실패 : "+url, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        StringBuffer responseBody = new StringBuffer();

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {

            String line;
            while( (line = lineReader.readLine()) != null ) {
                responseBody.append(line);
            }
        } catch (IOException e){
            throw new RuntimeException("API 응답 실패", e);
        }

        return responseBody.toString();
    }

    public static String get(String url, Map<String, String> header, String param) {
        String result = null;
        HttpURLConnection connection = connect(url+"?"+param);

        // FIXME:: Received fatal alert: handshake_failure 임시해결
        System.setProperty( "https.protocols", "TLSv1.2" );

        // API 요청
        try {
            connection.setRequestMethod("GET");

            // 헤더추가
            if( header!=null ) {
                for(Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            connection.setConnectTimeout(10000);    // timeout = 10초
            connection.setReadTimeout(5000);        // 컨텐츠 조회 timeout = 5초
            connection.setDoOutput(true);           // 항상 갱신된 내용 가져오도록

            // 요청 결과
            int responseCode = connection.getResponseCode();
            if( responseCode == HttpURLConnection.HTTP_OK ) result = readBody(connection.getInputStream());
            else result = readBody(connection.getErrorStream());

        } catch(IOException e) {
            throw new RuntimeException("API 요청 실패", e);
        } finally {
            connection.disconnect();
        }

        return result;
    }

    public static String post(String url, Map<String, String> header, String param) {
        String result = null;
        HttpURLConnection connection = connect(url);

        // FIXME:: Received fatal alert: handshake_failure 임시해결
        System.setProperty( "https.protocols", "TLSv1.2" );

        // API 요청
        try {
            connection.setRequestMethod("POST");

            // 헤더추가
            if( header!=null ) {
                for(Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 파라미터
            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.write(param.getBytes());
                dos.flush();
            }

            // 요청 설정
            connection.setConnectTimeout(10000);    // timeout = 10초
            connection.setReadTimeout(5000);        // 컨텐츠 조회 timeout = 5초
            connection.setDoOutput(true);           // 항상 갱신된 내용 가져오도록

            // 요청 결과
            int responseCode = connection.getResponseCode();
            if( responseCode == HttpURLConnection.HTTP_OK ) result = readBody(connection.getInputStream());
            else result = readBody(connection.getErrorStream());

        } catch(IOException e) {
            throw new RuntimeException("API 요청 실패", e);
        } finally {
            connection.disconnect();
        }

        return result;
    }

}