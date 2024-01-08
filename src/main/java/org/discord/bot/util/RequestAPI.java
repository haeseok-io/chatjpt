package org.discord.bot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class RequestAPI {

    public String requestGET(String url, Map<String, String> header) throws Exception {
        StringBuffer response = new StringBuffer();

        // FIXME:: Received fatal alert: handshake_failure 임시해결
        System.setProperty( "https.protocols", "TLSv1" );

        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod("GET");

        // 헤더정보가 있을경우 헤더정보 담기
        if( header!=null ){
            for(Map.Entry<String, String> entry : header.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        String inputLine;
        BufferedReader data = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while( (inputLine = data.readLine())!=null ){
            response.append(inputLine);
        }

        data.close();
        connection.disconnect();

        return response.toString();
    }
}