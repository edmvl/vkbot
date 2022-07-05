package ru.zhendozzz.vkbot.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.service.birthday.BirthdaySenderService;
import ru.zhendozzz.vkbot.service.horo.HoroSenderService;
import ru.zhendozzz.vkbot.service.horo.HoroService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;
import ru.zhendozzz.vkbot.service.weather.WeatherSenderService;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@Service
public class ProcessorJob {
    private final HoroService horoService;
    private final WeatherService weatherService;
    private final PhotoService photoService;
    private final HoroSenderService horoSenderService;
    private final WeatherSenderService weatherSenderService;
    private final BirthdaySenderService birthdaySenderService;


    public ProcessorJob(
            HoroService horoService, WeatherService weatherService, PhotoService photoService,
            HoroSenderService horoSenderService, WeatherSenderService weatherSenderService, BirthdaySenderService birthdaySenderService
    ) {
        this.horoService = horoService;
        this.weatherService = weatherService;
        this.photoService = photoService;
        this.horoSenderService = horoSenderService;
        this.weatherSenderService = weatherSenderService;
        this.birthdaySenderService = birthdaySenderService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        birthdaySenderService.congratsAllGroups();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void horo() {
        horoSenderService.sendHoroAll();
    }

    @Scheduled(cron = "0 0 4,16 * * ?")
    public void weather() {
        weatherSenderService.sendWeatherAll();
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
