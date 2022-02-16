package ru.zhendozzz.vkbot.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.service.HoroService;

@Service
public class HoroLoadJob {
    private final HoroService horoService;


    public HoroLoadJob(HoroService horoService) {
        this.horoService = horoService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void loadHoro() {
        horoService.grubDataFromResource();
    }
}
