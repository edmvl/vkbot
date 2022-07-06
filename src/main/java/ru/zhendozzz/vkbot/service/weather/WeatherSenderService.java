package ru.zhendozzz.vkbot.service.weather;

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

@Slf4j
@Service
public class WeatherSenderService {

    private final WeatherService weatherService;
    private final JobLogService jobLogService;
    private final GroupService groupService;
    private final BotUserService botUserService;
    private final VKService vkService;

    public WeatherSenderService(WeatherService weatherService, JobLogService jobLogService, GroupService groupService, BotUserService botUserService, VKService vkService) {
        this.weatherService = weatherService;
        this.jobLogService = jobLogService;
        this.groupService = groupService;
        this.botUserService = botUserService;
        this.vkService = vkService;
    }

    @SneakyThrows
    public String sendWeatherGroup(Integer groupId, String token, Integer vkUserId) {
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        try {
            String weather = weatherService.getWeather(localDate, groupId);
            if (weather.isEmpty()) {
                jobLogService.saveLog(groupId, LocalDate.now(), "Error: weather object is empty", false, JobType.WEATHER_SEND.getSysName());
                log.info("weather object is empty");
                return "Error";
            } else {
                vkService.postOnWall(actor, groupId, weather);
                log.info("finish posting successfully");
                jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.WEATHER_SEND.getSysName());
                return "Ok";
            }
        } catch (ClientException | ApiException e) {
            log.error(e.getMessage());
            jobLogService.saveLog(groupId, LocalDate.now(), e.getMessage(), false, JobType.WEATHER_SEND.getSysName());
            throw e;
        }
    }

    public String sendWeather(Integer groupId) {
        BotUser user = botUserService.getVkUser();
        return sendWeatherGroup(groupId, user.getToken(), user.getVkUserId());
    }

    public void sendWeatherAll(){
        List<Group> groups = groupService.getGroups();
        groups.forEach(group -> sendWeather(group.getGroupId()));
    }

    public String sendWeatherToGroups() {
        BotUser user = botUserService.getVkUser();
        List<Group> groups = groupService.getGroups();
        groups.forEach(botUser -> sendWeatherGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
        return "Ok";
    }

}
