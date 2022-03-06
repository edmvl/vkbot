package ru.zhendozzz.vkbot.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dto.joblog.JobLogDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogListDto;
import ru.zhendozzz.vkbot.service.BotUserService;
import ru.zhendozzz.vkbot.service.HoroService;
import ru.zhendozzz.vkbot.service.JobLogService;
import ru.zhendozzz.vkbot.service.VKService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final VKService vkService;
    private final BotUserService botUserService;
    private final JobLogService jobLogService;
    private final HoroService horoService;

    @Autowired
    public JobController(VKService vkService, BotUserService botUserService, JobLogService jobLogService, HoroService horoService) {
        this.vkService = vkService;
        this.botUserService = botUserService;
        this.jobLogService = jobLogService;
        this.horoService = horoService;
    }


    @GetMapping("/loadhoro")
    public ResponseEntity<Object> loadHoro() {
        horoService.grubDataFromResource();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sendhoro")
    public ResponseEntity<Object> sendHoro() {
        List<BotUser> allBotUsers = botUserService.getAllBotUsers();
        allBotUsers.forEach(botUser -> vkService.sendHoroGroup(botUser.getGroupId(), botUser.getToken(), botUser.getVkUserId()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/congrats")
    public ResponseEntity<Object> startCongrats() {
        List<BotUser> allBotUsers = botUserService.getAllBotUsers();
        allBotUsers.forEach(botUser -> vkService.congratsGroup(botUser.getGroupId(), botUser.getToken(), botUser.getVkUserId()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public JobLogListDto getAllLog() {
        return JobLogListDto.builder()
            .jobLogList(
                jobLogService.getAllLog().stream().map(
                    jobLog -> JobLogDto.builder()
                        .id(jobLog.getId())
                        .groupId(jobLog.getGroupId())
                        .date(jobLog.getDate())
                        .success(jobLog.getSuccess())
                        .type(jobLog.getType())
                        .text(jobLog.getText())
                        .build()
                ).collect(Collectors.toList())
            )
            .build();
    }
}
