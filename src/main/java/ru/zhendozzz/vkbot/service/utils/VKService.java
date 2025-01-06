package ru.zhendozzz.vkbot.service.utils;

import com.google.gson.Gson;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.UserXtrRole;
import com.vk.api.sdk.objects.groups.responses.GetMembersFieldsResponse;
import com.vk.api.sdk.objects.groups.responses.GetMembersResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.service.BotUserService;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VKService {

    private final BotUserService botUserService;

    private final VkApiClient vk;

    VKService(
            BotUserService botUserService
    ) {
        this.botUserService = botUserService;
        vk = new VkApiClient(new HttpTransportClient());
    }


    public List<UserFull> getMembersByBirthdayAndGroup(UserActor actor, Long groupId, LocalDate localDate) {
        log.info("start getting users with birthday");
        ArrayList<UserFull> res = new ArrayList<>();
        String birthday = localDate.get(ChronoField.DAY_OF_MONTH) + "." + localDate.get(ChronoField.MONTH_OF_YEAR);
        try {
            Integer offset = 0;
            Integer count = 1000;
            Integer respCount;
            do {
                GetMembersFieldsResponse resp = vk.groups().getMembersWithFields(actor, Fields.BDATE)
                        .groupId(groupId.toString())
                        .count(count)
                        .offset(offset * count)
                        .execute();
                respCount = resp.getCount();
                resp.getItems().stream()
                        .filter(userXtrRole -> birthday.equals(userXtrRole.getBdate()))
                        .forEach(res::add);
                offset++;
            } while (respCount > count * offset);
            log.info("finish getting users with birthday with result:");
            return res;
        } catch (ClientException | ApiException exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public void postOnWall(UserActor actor, Long groupId, String message, String attachments) throws ClientException, ApiException {
        vk.wall()
                .post(actor)
                .fromGroup(true)
                .ownerId(-groupId)
                .attachments(attachments)
                .message(message)
                .execute();
    }

    public void postOnWall(UserActor actor, Long groupId, String message) throws ClientException, ApiException {
        postOnWall(actor, groupId, message, null);
    }

    @SneakyThrows
    public List<Photo> getPhotosFromAlbum(String albumId, Long groupId) {
        BotUser user = botUserService.getVkUser();
        UserActor actor = new UserActor(user.getVkUserId(), user.getToken());
        return vk.photos().get(actor).albumId(albumId).ownerId(-groupId).count(1000).execute().getItems();
    }

    @SneakyThrows
    public void sendPhotoToGroup(Long groupId, String attachments) {
        BotUser user = botUserService.getVkUser();
        UserActor actor = new UserActor(user.getVkUserId(), user.getToken());
        postOnWall(actor, groupId, "", attachments);
    }
}
