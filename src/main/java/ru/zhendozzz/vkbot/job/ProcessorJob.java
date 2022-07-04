package ru.zhendozzz.vkbot.job;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.service.GroupService;
import ru.zhendozzz.vkbot.service.HoroService;
import ru.zhendozzz.vkbot.service.VKService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@Service
public class ProcessorJob {
    private final GroupService groupService;
    private final VKService vkService;
    private final HoroService horoService;
    private final WeatherService weatherService;
    private final PhotoService photoService;

    public ProcessorJob(
            GroupService groupService, VKService vkService, HoroService horoService, WeatherService weatherService,
            PhotoService photoService) {
        this.groupService = groupService;
        this.vkService = vkService;
        this.horoService = horoService;
        this.weatherService = weatherService;
        this.photoService = photoService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> vkService.congratsGroup(botUser.getGroupId()));
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void horo() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(vkService::sendHoro);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void weather1() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> vkService.sendWeather(botUser.getGroupId()));
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void weather2() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> vkService.sendWeather(botUser.getGroupId()));
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void loadWeather1() {
        weatherService.downloadWeather();
    }

    @Scheduled(cron = "0 0 15 * * ?")
    public void loadWeather2() {
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
