package ru.zhendozzz.vkbot.job;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.service.BotUserService;
import ru.zhendozzz.vkbot.service.VKService;

@Service
public class VkGroupProcessorJob {
    private final VKService vkService;
    private final BotUserService botUserService;

    public VkGroupProcessorJob(VKService userSearchService, BotUserService botUserService) {
        this.vkService = userSearchService;
        this.botUserService = botUserService;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void congrats() {
        List<BotUser> allBotUsers = botUserService.getAllBotUsers();
        allBotUsers.forEach(botUser -> vkService.processGroup(
            botUser.getGroupId(), botUser.getToken(), botUser.getVkUserId())
        );
    }
}
