package ru.zhendozzz.vkbot.service.birthday;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.enums.JobType;
import ru.zhendozzz.vkbot.service.BotUserService;
import ru.zhendozzz.vkbot.service.group.GroupService;
import ru.zhendozzz.vkbot.service.JobLogService;
import ru.zhendozzz.vkbot.service.VKService;
import ru.zhendozzz.vkbot.service.utils.TextFormatterService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BirthdaySenderService {

    private final JobLogService jobLogService;
    private final VKService vkService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    private final TextFormatterService textFormatterService;


    public BirthdaySenderService(JobLogService jobLogService, VKService vkService, GroupService groupService, BotUserService botUserService, TextFormatterService textFormatterService) {
        this.jobLogService = jobLogService;
        this.vkService = vkService;
        this.groupService = groupService;
        this.botUserService = botUserService;
        this.textFormatterService = textFormatterService;
    }

    public void congratsGroup(Integer groupId, String token, Integer vkUserId) {
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(groupId, localDate, JobType.VK_CONGRATS.getSysName())) {
            try {
                List<UserFull> members = vkService.getMembersByBirthdayAndGroup(actor, groupId, localDate);
                log.info("start posting");
                if (CollectionUtils.isNotEmpty(members)) {
                    String s = textFormatterService.getTextForBirthDay(members);
                    String attachments = attachUserProfilePhoto(members);
                    vkService.postOnWall(actor, groupId, s, attachments);
                }
                log.info("finish posting successfully");
                jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.VK_CONGRATS.getSysName());
            } catch (ClientException | ApiException e) {
                log.error(e.getMessage());
                jobLogService.saveLog(groupId, LocalDate.now(), "Error", false, JobType.VK_CONGRATS.getSysName());
            }
        }
    }

    public void congratsAllGroups() {
        BotUser user = botUserService.getUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> congratsGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
    }

    private String attachUserProfilePhoto(List<UserFull> members) {
        return members.stream()
                .map(member -> "photo" + member.getPhotoId()).collect(Collectors.joining(", "));
    }

}
