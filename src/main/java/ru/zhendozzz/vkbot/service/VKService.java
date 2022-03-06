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
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.enums.JobType;

@Service
@Slf4j
public class VKService {

    @Qualifier("httpTransportClient")
    private final TransportClient transportClient;
    private final TextFormatterService textFormatterService;
    private final VkImageService vkImageService;
    private final HoroService horoService;
    private final JobLogService jobLogService;

    VKService(TextFormatterService textFormatterService, TransportClient transportClient, VkImageService vkImageService, HoroService horoService, JobLogService jobLogService) {
        this.textFormatterService = textFormatterService;
        this.transportClient = transportClient;
        this.vkImageService = vkImageService;
        this.horoService = horoService;
        this.jobLogService = jobLogService;
    }

    public void congratsGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (!jobLogService.isGroupProcessed(groupId, localDate, JobType.VK_CONGRATS.getSysName())) {
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

    public void sendHoroGroup(Integer groupId, String token, Integer vkUserId) {
        VkApiClient vk = new VkApiClient(this.transportClient);
        UserActor actor = new UserActor(vkUserId, token);
        LocalDate localDate = LocalDate.now();
        if (!jobLogService.isGroupProcessed(groupId, localDate, JobType.HORO_SEND.getSysName())) {
            try {
                String horoToDate = horoService.getHoroToDate(LocalDate.now());
                postOnWall(vk, actor, groupId, horoToDate);
            } catch (ClientException | ApiException e) {
                log.error(e.getMessage());
                jobLogService.saveLog(groupId, LocalDate.now(), "Error", false, JobType.HORO_SEND.getSysName());
            }
            log.info("finish posting successfully");
            jobLogService.saveLog(groupId, LocalDate.now(), "Ok", true, JobType.HORO_SEND.getSysName());
        }
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
