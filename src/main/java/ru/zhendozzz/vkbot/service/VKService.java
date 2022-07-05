package ru.zhendozzz.vkbot.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.responses.OkResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.BotUser;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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


    public List<UserFull> getMembersByBirthdayAndGroup(UserActor actor, Integer groupId, LocalDate localDate) {
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
        UserActor actor = new UserActor(vkUserId, token);
        OkResponse execute = vk.groups().join(actor).groupId(groupId).execute();
        return execute.getValue();
    }

    public void postOnWall(UserActor actor, Integer groupId, String message, String attachments) throws ClientException, ApiException {
        vk.wall()
                .post(actor)
                .fromGroup(true)
                .ownerId(-groupId)
                .attachments(attachments)
                .message(message)
                .execute();
    }

    public void postOnWall(UserActor actor, Integer groupId, String message) throws ClientException, ApiException {
        postOnWall(actor, groupId, message, null);
    }

    @SneakyThrows
    public List<Photo> getPhotosFromAlbum(String albumId, Integer groupId) {
        BotUser user = botUserService.getUser();
        UserActor actor = new UserActor(user.getVkUserId(), user.getToken());
        return vk.photos().get(actor).albumId(albumId).ownerId(-groupId).count(1000).execute().getItems();
    }

    @SneakyThrows
    public void sendPhotoToGroup(Integer groupId, String attachments) {
        BotUser user = botUserService.getUser();
        UserActor actor = new UserActor(user.getVkUserId(), user.getToken());
        postOnWall(actor, groupId, "", attachments);
    }
}
