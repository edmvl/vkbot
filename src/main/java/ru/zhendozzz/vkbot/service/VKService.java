package ru.zhendozzz.vkbot.service;

import java.time.LocalDate;
import java.util.List;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Scope("singleton")
public class VKService {

    TransportClient transportClient;
    VkApiClient vk;
    UserActor actor;

    VKService() {
        transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(6903594, "a37435383309d0a773134da74a46c57d28cc7ff924630a453bf951db5d3bb5a7ac47a9d4c237d4db4767b");
    }

    public List<UserFull> getMembersWithBirthdayToday(Integer groupId) throws ClientException, ApiException {
        log.info("start getting users with birthday");
        LocalDate localDate = LocalDate.now();

        SearchResponse execute = vk.users().search(actor).groupId(groupId)
            .fields(Fields.DOMAIN)
            .birthDay(localDate.getDayOfMonth())
            .birthMonth(localDate.getMonthValue())
            .execute();
        log.info("finish getting users with birthday with result:" + execute.getItems().toString());
        return execute.getItems();
    }

    public void postOnWall(Integer groupId, String message) {
        log.info("start posting");
        try {
            vk.wall().post(actor).fromGroup(true).ownerId(-groupId).message(message).execute();
            log.info("finish posting successfully");
        }catch (ApiException | ClientException exception){
            log.error(exception.getMessage());
        }
    }
}
