package org.discord.bot;

import org.discord.bot.util.WeatherData;

import java.io.IOException;
import java.util.List;

public class TestMain {
    public static void main(String[] args) throws IOException {
        //String apiKey = "Xt0cUVgX7VOk%2FpDmBxslTm7SLwbGzgTElJ21zTVv3rGvij61O40z9mfztRNf%2BP8ICeJpXeMr%2Fv%2BD2tzNXsHtxQ%3D%3D";
        //String endPoint = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        //
        //String param = "ServiceKey="+apiKey+"&pageNo=1&numOfRows=10000&dataType=json&base_date=20240122&base_time=0600&nx=55&ny=127";
        //String jsonData = RequestAPI.get(endPoint, null, param);
        //
        //System.out.println(jsonData);

        WeatherData wd = new WeatherData();
        List<String> list = wd.getWeatherSi();

        System.out.println(list);
    }

}
