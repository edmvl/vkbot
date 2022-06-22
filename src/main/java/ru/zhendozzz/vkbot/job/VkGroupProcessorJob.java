package ru.zhendozzz.vkbot.job;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.service.GroupService;
import ru.zhendozzz.vkbot.service.VKService;

@Service
public class VkGroupProcessorJob {
    private final GroupService groupService;
    private final VKService vkService;

    public VkGroupProcessorJob(GroupService groupService, VKService vkService) {
        this.groupService = groupService;
        this.vkService = vkService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> {
                    Integer groupId = botUser.getGroupId();
                    vkService.congratsGroup(groupId);
                    vkService.sendHoro(groupId);
                    vkService.sendWeather(groupId);
                }
        );
    }
}
