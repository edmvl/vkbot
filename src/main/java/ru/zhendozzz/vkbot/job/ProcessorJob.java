package ru.zhendozzz.vkbot.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.service.birthday.BirthdaySenderService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;

@Service
public class ProcessorJob {
    private final PhotoService photoService;
    private final BirthdaySenderService birthdaySenderService;


    public ProcessorJob(
             PhotoService photoService, BirthdaySenderService birthdaySenderService
    ) {
        this.photoService = photoService;
        this.birthdaySenderService = birthdaySenderService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        birthdaySenderService.congratsAllGroups();
    }

    @Scheduled(cron = "0 0 10,13,17 * * ?")
    public void sendPhotoFromAlbum() {
        photoService.sendPhotoToGroups();
    }

}
