package ru.zhendozzz.vkbot.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import ru.zhendozzz.vkbot.dto.common.CommonDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogDto;
import ru.zhendozzz.vkbot.dto.joblog.JobLogListDto;
import ru.zhendozzz.vkbot.service.birthday.BirthdaySenderService;
import ru.zhendozzz.vkbot.service.horo.HoroSenderService;
import ru.zhendozzz.vkbot.service.horo.HoroService;
import ru.zhendozzz.vkbot.service.JobLogService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhendozzz.vkbot.service.VKService;
import ru.zhendozzz.vkbot.service.weather.WeatherSenderService;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobLogService jobLogService;
    private final HoroService horoService;
    private final VKService vkService;
    private final WeatherService weatherService;
    private final HoroSenderService horoSenderService;
    private final WeatherSenderService sendWeatherToGroups;
    private final BirthdaySenderService birthdaySenderService;

    @Autowired
    public JobController(JobLogService jobLogService, HoroService horoService, VKService vkService, WeatherService weatherService, HoroSenderService horoSenderService, WeatherSenderService sendWeatherToGroups, BirthdaySenderService birthdaySenderService) {
        this.jobLogService = jobLogService;
        this.horoService = horoService;
        this.vkService = vkService;
        this.weatherService = weatherService;
        this.horoSenderService = horoSenderService;
        this.sendWeatherToGroups = sendWeatherToGroups;
        this.birthdaySenderService = birthdaySenderService;
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
        String s = horoSenderService.sendHoroToGroups();
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/sendweather")
    public CommonDto sendWeather() {
        String s = sendWeatherToGroups.sendWeatherToGroups();
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/loadweather")
    public CommonDto loadWeather() {
        weatherService.downloadWeather();
        return CommonDto.builder()
            .result("Ok")
            .build();
    }

    @GetMapping("/sendhoro/{groupId}")
    public CommonDto sendHoroToGroup(@PathVariable("groupId") Integer groupId) {
        String s = horoSenderService.sendHoro(groupId);
        return CommonDto.builder()
            .result(s)
            .build();
    }

    @GetMapping("/weather/{groupId}")
    public CommonDto sendWeatherToGroup(@PathVariable("groupId") Integer groupId) {
        String s = sendWeatherToGroups.sendWeather(groupId);
        return CommonDto.builder()
            .result(s)
            .build();
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
