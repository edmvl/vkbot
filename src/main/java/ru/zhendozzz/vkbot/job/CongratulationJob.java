package ru.zhendozzz.vkbot.job;

import java.util.List;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.service.TextFormatterService;
import ru.zhendozzz.vkbot.service.VKService;

@Service
public class CongratulationJob {
    private final VKService vkService;
    private final TextFormatterService textFormatterService;

    public CongratulationJob(VKService userSearchService, TextFormatterService textFormatterService) {
        this.vkService = userSearchService;
        this.textFormatterService = textFormatterService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void congrats() throws ClientException, ApiException {
        int groupId = 5574803;
        List<UserFull> members = vkService.getMembersWithBirthdayToday(groupId);
        if (CollectionUtils.isNotEmpty(members)) {
            String s = textFormatterService.getTextForBirthDay(members);
            vkService.postOnWall(groupId, s);
        }
    }
}
