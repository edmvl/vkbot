package ru.zhendozzz.vkbot.service.horo;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.enums.JobType;
import ru.zhendozzz.vkbot.service.BotUserService;
import ru.zhendozzz.vkbot.service.group.GroupService;
import ru.zhendozzz.vkbot.service.JobLogService;
import ru.zhendozzz.vkbot.service.utils.VKService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class HoroSenderService {

    private final HoroLoaderService horoLoaderService;
    private final JobLogService jobLogService;
    private final VKService vkService;
    private final BotUserService botUserService;
    private final GroupService groupService;

    public HoroSenderService(
            HoroLoaderService horoLoaderService, JobLogService jobLogService, VKService vkService, BotUserService botUserService,
            GroupService groupService
    ) {
        this.horoLoaderService = horoLoaderService;
        this.jobLogService = jobLogService;
        this.vkService = vkService;
        this.botUserService = botUserService;
        this.groupService = groupService;
    }

    @SneakyThrows
    public String sendHoroGroup(Integer groupId, String token, Integer vkUserId) {
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(groupId, localDate, JobType.HORO_SEND.getSysName())) {
            try {
                String horoToDate = horoLoaderService.getHoroToDate(LocalDate.now());
                vkService.postOnWall(actor, groupId, horoToDate);
                log.info("finish posting successfully");
                jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.HORO_SEND.getSysName());
                return "Ok";
            } catch (ClientException | ApiException e) {
                log.error(e.getMessage());
                jobLogService.saveLog(groupId, LocalDate.now(), e.getMessage(), false, JobType.HORO_SEND.getSysName());
                throw e;
            }
        } else {
            log.info("Дублированный запрос, Гороскоп уже опубликован ранее");
            jobLogService.saveLog(groupId, LocalDate.now(), "Duplicated", false, JobType.HORO_SEND.getSysName());
            return "Гороскоп уже опубликован";
        }
    }

    public String sendHoro(Integer groupId) {
        Optional<Group> groupByGroupId = groupService.findGroupByGroupId(groupId);
        return groupByGroupId.map(this::sendHoro).orElse(null);
    }

    public String sendHoro(Group group) {
        BotUser user = botUserService.getVkUser();
        Map<String, String> setting = group.getSetting();
        if (Objects.nonNull(setting) && "true".equals(setting.get("horo_enabled"))) {
            return sendHoroGroup(group.getGroupId(), user.getToken(), user.getVkUserId());
        } else {
            return "Гороскоп отключен для сообщества";
        }
    }

    public void sendHoroAll() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(this::sendHoro);
    }

}
