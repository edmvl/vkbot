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
import ru.zhendozzz.vkbot.service.VKService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HoroSenderService {

    private final HoroService horoService;
    private final JobLogService jobLogService;
    private final VKService vkService;
    private final BotUserService botUserService;
    private final GroupService groupService;

    public HoroSenderService(
            HoroService horoService, JobLogService jobLogService, VKService vkService, BotUserService botUserService,
            GroupService groupService
    ) {
        this.horoService = horoService;
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
                String horoToDate = horoService.getHoroToDate(LocalDate.now());
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

    public String sendHoroToGroups() {
        BotUser user = botUserService.getUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(group -> sendHoroGroup(group.getGroupId(), user.getToken(), user.getVkUserId()));
        return "Ok";
    }

    public String sendHoro(Integer groupId) {
        BotUser user = botUserService.getUser();
        Optional<Group> groupByGroupId = groupService.findGroupByGroupId(groupId);
        if (groupByGroupId.isPresent()) {
            Group group = groupByGroupId.get();
            String horoEnabled = group.getSetting().get("horo_enabled");
            if ("true".equals(horoEnabled)) {
                return sendHoroGroup(group.getGroupId(), user.getToken(), user.getVkUserId());
            } else {
                return "Гороскоп отключен для сообщества";
            }
        }
        return null;
    }

    public String sendHoro(Group group) {
        BotUser user = botUserService.getUser();
        String horoEnabled = group.getSetting().get("horo_enabled");
        if ("true".equals(horoEnabled)) {
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