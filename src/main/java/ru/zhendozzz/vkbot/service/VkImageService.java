package ru.zhendozzz.vkbot.service;

import java.util.List;

import com.vk.api.sdk.objects.users.UserFull;
import org.springframework.stereotype.Service;

@Service
public class VkImageService {
    public String getAttachments(List<UserFull> members) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            UserFull userFull = members.get(i);
            result
                .append("photo")
                .append(userFull.getPhotoId())
                .append(",");

        }
        return result.toString();
    }
}
