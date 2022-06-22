package ru.zhendozzz.vkbot.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import ru.zhendozzz.vkbot.dto.common.CommonDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogListDto;
import ru.zhendozzz.vkbot.service.GroupService;
import ru.zhendozzz.vkbot.service.HoroService;
import ru.zhendozzz.vkbot.service.JobLogService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhendozzz.vkbot.service.VKService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobLogService jobLogService;
    private final HoroService horoService;
    private final VKService vkService;

    @Autowired
    public JobController(JobLogService jobLogService, HoroService horoService, VKService vkService) {
        this.jobLogService = jobLogService;
        this.horoService = horoService;
        this.vkService = vkService;
    }


    @GetMapping("/loadhoro")
    public CommonDto loadHoro() {
        String s = horoService.grubDataFromResource();
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/sendhoro")
    public CommonDto sendHoro() {
        String s = vkService.sendHoroToGroups();
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/sendweather")
    public CommonDto sendWeather() {
        String s = vkService.sendWeatherToGroups();
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/sendhoro/{groupId}")
    public CommonDto sendHoroToGroup(@PathVariable("groupId") Integer groupId) {
        String s = vkService.sendHoro(groupId);
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/weather/{groupId}")
    public CommonDto sendWeatherToGroup(@PathVariable("groupId") Integer groupId) {
        String s = vkService.sendWeather(groupId);
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/congrats")
    public ResponseEntity<Object> startCongrats() {
        vkService.congratsAllGroups();
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
