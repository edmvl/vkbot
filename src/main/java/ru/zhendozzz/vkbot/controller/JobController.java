package ru.zhendozzz.vkbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhendozzz.vkbot.dto.joblog.JobLogDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogListDto;
import ru.zhendozzz.vkbot.service.JobLogService;
import ru.zhendozzz.vkbot.service.birthday.BirthdaySenderService;
import ru.zhendozzz.vkbot.service.utils.VKService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobLogService jobLogService;
    private final BirthdaySenderService birthdaySenderService;

    @Autowired
    public JobController(JobLogService jobLogService, VKService vkService, BirthdaySenderService birthdaySenderService
    ) {
        this.jobLogService = jobLogService;
        this.birthdaySenderService = birthdaySenderService;
    }


    @GetMapping("/congrats")
    public ResponseEntity<Object> startCongrats() {
        birthdaySenderService.congratsAllGroups();
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
