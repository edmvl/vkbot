package ru.zhendozzz.vkbot.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Scope("singleton")
public class VKService {

    @Qualifier("httpTransportClient")
    private final TransportClient transportClient;
    private final TextFormatterService textFormatterService;

    VKService(TextFormatterService textFormatterService, TransportClient transportClient) {
        this.textFormatterService = textFormatterService;
        this.transportClient = transportClient;
    }

    public void processGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        List<UserFull> members = getMembersWithBirthdayToday(vk, actor, groupId);
        if (CollectionUtils.isNotEmpty(members)) {
            String s = textFormatterService.getTextForBirthDay(members);
            postOnWall(vk, actor, groupId, s);
        }
    }

    private List<UserFull> getMembersWithBirthdayToday(VkApiClient vk, UserActor actor, Integer groupId) {
        log.info("start getting users with birthday");
        LocalDate localDate = LocalDate.now();
        SearchResponse execute;
        try {
            execute = vk.users().search(actor).groupId(groupId)
                .fields(Fields.DOMAIN)
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

    private void postOnWall(VkApiClient vk, UserActor actor, Integer groupId, String message) {
        log.info("start posting");
        try {
            vk.wall().post(actor).fromGroup(true).ownerId(-groupId).message(message).execute();
            log.info("finish posting successfully");
        } catch (ApiException | ClientException exception) {
            log.error(exception.getMessage());
        }
    }
}
