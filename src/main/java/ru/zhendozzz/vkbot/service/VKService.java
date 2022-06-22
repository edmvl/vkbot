package ru.zhendozzz.vkbot.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.responses.OkResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.enums.JobType;
import ru.zhendozzz.vkbot.service.weather.WeatherService;

@Service
@Slf4j
public class VKService {

    @Qualifier("httpTransportClient")
    private final TransportClient transportClient;
    private final TextFormatterService textFormatterService;
    private final VkImageService vkImageService;
    private final HoroService horoService;
    private final JobLogService jobLogService;
    private final WeatherService weatherService;
    private final BotUserService botUserService;
    private final GroupService groupService;


    VKService(TextFormatterService textFormatterService, TransportClient transportClient, VkImageService vkImageService, HoroService horoService, JobLogService jobLogService, WeatherService weatherService, BotUserService botUserService, GroupService groupService) {
        this.textFormatterService = textFormatterService;
        this.transportClient = transportClient;
        this.vkImageService = vkImageService;
        this.horoService = horoService;
        this.jobLogService = jobLogService;
        this.weatherService = weatherService;
        this.botUserService = botUserService;
        this.groupService = groupService;
    }

    public void congratsGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(groupId, localDate, JobType.VK_CONGRATS.getSysName())) {
            try {
                List<UserFull> members = getMembersByBirthdayAndGroup(vk, actor, groupId, localDate);
                log.info("start posting");
                if (CollectionUtils.isNotEmpty(members)) {
                    String s = textFormatterService.getTextForBirthDay(members);
                    String attachments = vkImageService.getAttachments(members);
                    postOnWall(vk, actor, groupId, s, attachments);
                }
                log.info("finish posting successfully");
                jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.VK_CONGRATS.getSysName());
            } catch (ClientException | ApiException e) {
                log.error(e.getMessage());
                jobLogService.saveLog(groupId, LocalDate.now(), "Error", false, JobType.VK_CONGRATS.getSysName());
            }
        }
    }

    @SneakyThrows
    public String sendHoroGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(groupId, localDate, JobType.HORO_SEND.getSysName())) {
            try {
                String horoToDate = horoService.getHoroToDate(LocalDate.now());
                postOnWall(vk, actor, groupId, horoToDate);
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

    @SneakyThrows
    public String sendWeatherGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(groupId, localDate, JobType.WEATHER_SEND.getSysName())) {
            try {
                String weather = weatherService.getWeather(localDate, groupId);
                postOnWall(vk, actor, groupId, weather);
                log.info("finish posting successfully");
                jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.WEATHER_SEND.getSysName());
                return "Ok";
            } catch (ClientException | ApiException e) {
                log.error(e.getMessage());
                jobLogService.saveLog(groupId, LocalDate.now(), e.getMessage(), false, JobType.WEATHER_SEND.getSysName());
                throw e;
            }
        } else {
            log.info("Дублированный запрос, Прогноз уже опубликован ранее");
            jobLogService.saveLog(groupId, LocalDate.now(), "Duplicated", false, JobType.WEATHER_SEND.getSysName());
            return "Прогноз уже опубликован";
        }
    }

    public String sendHoro(Integer groupId) {
        BotUser user = botUserService.getUser();
        return sendHoroGroup(groupId, user.getToken(), user.getVkUserId());
    }

    public String sendWeather(Integer groupId) {
        BotUser user = botUserService.getUser();
        return sendWeatherGroup(groupId, user.getToken(), user.getVkUserId());
    }

    public String sendHoroToGroups() {
        BotUser user = botUserService.getUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> sendHoroGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
        return "Ok";
    }

    public String sendWeatherToGroups() {
        BotUser user = botUserService.getUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> sendWeatherGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
        return "Ok";
    }

    public void congratsAllGroups() {
        BotUser user = botUserService.getUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> congratsGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
    }

    public void congratsGroup(Integer groupId) {
        BotUser user = botUserService.getUser();
        congratsGroup(groupId, user.getToken(), user.getVkUserId());
    }

    @SneakyThrows
    public String inviteModerator(Integer groupId){
        BotUser user = botUserService.getUser();
        String s = inviteModerator(groupId, user.getToken(), user.getVkUserId());
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

    private List<UserFull> getMembersByBirthdayAndGroup(VkApiClient vk, UserActor actor, Integer groupId, LocalDate localDate) {
        log.info("start getting users with birthday");
        SearchResponse execute;
        try {
            execute = vk.users().search(actor).groupId(groupId)
                    .fields(Fields.DOMAIN, Fields.PHOTO, Fields.PHOTO_ID)
                    .birthDay(localDate.getDayOfMonth())
                    .birthMonth(localDate.getMonthValue())
                    .execute();
            log.info("finish getting users with birthday with result:" + execute.getItems().toString());
            return execute.getItems();
        } catch (ApiException | ClientException exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public String inviteModerator(Integer groupId, String token, Integer vkUserId) throws ClientException, ApiException {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        OkResponse execute = vk.groups().join(actor).groupId(groupId).execute();
        return execute.getValue();
    }

    private void postOnWall(VkApiClient vk, UserActor actor, Integer groupId, String message, String attachments) throws ClientException, ApiException {
        vk.wall()
                .post(actor)
                .fromGroup(true)
                .ownerId(-groupId)
                .attachments(attachments)
                .message(message)
                .execute();
    }

    private void postOnWall(VkApiClient vk, UserActor actor, Integer groupId, String message) throws ClientException, ApiException {
        postOnWall(vk, actor, groupId, message, null);
    }
}
