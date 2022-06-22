package ru.zhendozzz.vkbot.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@Service
public class WeatherLoadJob {
    private final WeatherService weatherService;

    public WeatherLoadJob(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void loadWeather() {
        weatherService.downloadWeather();
    }
}
