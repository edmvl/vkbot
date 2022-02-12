package ru.zhendozzz.vkbot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.service.BotUserService;
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

    @Autowired
    public JobController(VKService vkService, BotUserService botUserService) {
        this.vkService = vkService;
        this.botUserService = botUserService;
    }


    @GetMapping("/start")
    public ResponseEntity<Object> startCongrats() {
        List<BotUser> allBotUsers = botUserService.getAllBotUsers();
        allBotUsers.forEach(botUser -> vkService.processGroup(botUser.getGroupId(), botUser.getToken(), botUser.getVkUserId()));
        return ResponseEntity.ok().build();
    }
}
