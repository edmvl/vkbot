package ru.zhendozzz.vkbot.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.service.HoroService;
import ru.zhendozzz.vkbot.service.VKService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@Service
public class ProcessorJob {
    private final VKService vkService;
    private final HoroService horoService;
    private final WeatherService weatherService;
    private final PhotoService photoService;

    public ProcessorJob(
            VKService vkService, HoroService horoService, WeatherService weatherService, PhotoService photoService
    ) {
        this.vkService = vkService;
        this.horoService = horoService;
        this.weatherService = weatherService;
        this.photoService = photoService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        vkService.congratsAllGroups();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void horo() {
        vkService.sendHoroAll();
    }

    @Scheduled(cron = "0 0 4,16 * * ?")
    public void weather() {
        vkService.sendWeatherAll();
    }

    @Scheduled(cron = "0 0 3,15 * * ?")
    public void loadWeather() {
        weatherService.downloadWeather();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void loadHoro() {
        horoService.grubDataFromResource();
    }

    @Scheduled(cron = "0 0 10,13,17 * * ?")
    public void sendPhotoFromAlbum() {
        photoService.sendPhotoToGroups();
    }

}
