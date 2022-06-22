package ru.zhendozzz.vkbot.service.weather;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

@Service
public class WeatherLoaderService {

    @SneakyThrows
    private String getWeatherFromResource(String lat, String lon, String key) {
        String url = "https://api.weather.yandex.ru/v2/informers?lat=" + lat + "&lon=" + lon;
        Connection.Response execute = Jsoup.connect(url)
                .header("X-Yandex-API-Key", key)
                .ignoreContentType(true)
                .execute();
        return execute.body();
    }

    public String getWeatherObject(String lat, String lon) {
        return getWeatherFromResource(lat, lon, "5aeb8e8b-f649-4cf1-a734-09c99cf7fc30");
    }

}
