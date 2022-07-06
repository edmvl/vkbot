package ru.zhendozzz.vkbot.service.group;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.service.BotUserService;
import ru.zhendozzz.vkbot.service.utils.VKService;

import java.util.Map;
import java.util.Optional;

@Service
public class GroupManagementService {

    private final GroupService groupService;
    private final BotUserService botUserService;
    private final VKService vkService;

    public GroupManagementService(GroupService groupService, BotUserService botUserService, VKService vkService) {
        this.groupService = groupService;
        this.botUserService = botUserService;
        this.vkService = vkService;
    }

    @SneakyThrows
    public String inviteModerator(Integer groupId) {
        BotUser user = botUserService.getVkUser();
        String s = vkService.inviteModerator(groupId, user.getToken(), user.getVkUserId());
        Optional<Group> byId = groupService.findGroupByGroupId(groupId);
        if (byId.isPresent()) {
            Group foundGroup = byId.get();
            Map<String, String> setting = foundGroup.getSetting();
            setting.put("moderInvited", "true");
            groupService.save(foundGroup);
        } else {
            throw new Exception();
        }
        return s;
    }

}
