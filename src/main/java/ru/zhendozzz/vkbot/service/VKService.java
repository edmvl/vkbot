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
    private final HoroService horoService;
    private final JobLogService jobLogService;

    VKService(TextFormatterService textFormatterService, TransportClient transportClient, HoroService horoService, JobLogService jobLogService) {
        this.textFormatterService = textFormatterService;
        this.transportClient = transportClient;
        this.horoService = horoService;
        this.jobLogService = jobLogService;
    }

    public void processGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (jobLogService.isGroupProcessed(groupId, localDate)) {
            log.info("Group " + groupId + " already has been processed");
            return;
        }
        List<UserFull> members = getMembersByBirthdayAndGroup(vk, actor, groupId, localDate);
        try {
            log.info("start posting");
            if (CollectionUtils.isNotEmpty(members)) {
                String s = textFormatterService.getTextForBirthDay(members);
                postOnWall(vk, actor, groupId, s);
            }
            String horoToDate = horoService.getHoroToDate(LocalDate.now());
            postOnWall(vk, actor, groupId, horoToDate);
            log.info("finish posting successfully");
            jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true);
        } catch (ClientException | ApiException e) {
            log.error(e.getMessage());
            jobLogService.saveLog(groupId, LocalDate.now(), "Error", false);
        }
    }

    private List<UserFull> getMembersByBirthdayAndGroup(VkApiClient vk, UserActor actor, Integer groupId, LocalDate localDate) {
        log.info("start getting users with birthday");
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

    private void postOnWall(VkApiClient vk, UserActor actor, Integer groupId, String message) throws ClientException, ApiException {
        vk.wall().post(actor).fromGroup(true).ownerId(-groupId).message(message).execute();
    }
}
