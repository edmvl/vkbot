package ru.zhendozzz.vkbot.controller;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.zhendozzz.vkbot.dto.botuser.BotUserDto;
import ru.zhendozzz.vkbot.dto.botuser.BotUserListDto;
import ru.zhendozzz.vkbot.service.BotUserService;

@RestController
@RequestMapping("/api/v1/botuser")
public class BotUserController {
    private final BotUserService botUserService;

    public BotUserController(BotUserService botUserService) {
        this.botUserService = botUserService;
    }

    @GetMapping("/list")
    public BotUserListDto getAllUsers(){
        return BotUserListDto.builder()
            .botUserList(
                botUserService.getAllBotUsers().stream().map(
                    botUser -> BotUserDto.builder()
                        .id(botUser.getId())
                        .vkUserId(botUser.getVkUserId())
                        .groupId(botUser.getGroupId())
                        .token(botUser.getToken())
                        .build()
                ).collect(Collectors.toList())
            )
            .build();
    }

}
