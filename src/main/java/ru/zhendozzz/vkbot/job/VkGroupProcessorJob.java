package ru.zhendozzz.vkbot.job;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.service.GroupService;

@Service
public class VkGroupProcessorJob {
    private final GroupService groupService;

    public VkGroupProcessorJob(GroupService groupService) {
        this.groupService = groupService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> {
                groupService.congratsGroup(botUser.getGroupId());
                groupService.sendHoro(botUser.getGroupId());
            }
        );
    }
}
