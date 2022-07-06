package ru.zhendozzz.vkbot.service.photoposter;

import com.vk.api.sdk.objects.photos.Image;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.entity.VkPhoto;
import ru.zhendozzz.vkbot.dao.repository.VkPhotoRepository;
import ru.zhendozzz.vkbot.service.group.GroupService;
import ru.zhendozzz.vkbot.service.utils.TgBotService;
import ru.zhendozzz.vkbot.service.utils.VKService;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final VKService vkService;
    private final TgBotService tgBotService;
    private final GroupService groupService;
    private final VkPhotoRepository vkPhotoRepository;

    public PhotoService(VKService vkService, TgBotService tgBotService, GroupService groupService, VkPhotoRepository vkPhotoRepository) {
        this.vkService = vkService;
        this.tgBotService = tgBotService;
        this.groupService = groupService;
        this.vkPhotoRepository = vkPhotoRepository;
    }


    public void loadPhotoListFromAlbum(String albumId, Integer groupId) {
        List<Photo> photosFromAlbum = vkService.getPhotosFromAlbum(albumId, groupId);
        List<VkPhoto> vkPhotoList = photosFromAlbum.stream().map(
                photo -> VkPhoto.builder()
                        .vkImageId("photo" + photo.getOwnerId() + "_" + photo.getId().toString())
                        .groupId(groupId)
                        .url(photo.getSizes().stream().max(Comparator.comparingInt(PhotoSizes::getWidth)).orElseGet(null).getUrl().toString())
                        .build()
        ).collect(Collectors.toList());

        vkPhotoRepository.saveAll(vkPhotoList);
    }

    public void sendPhotoToGroups() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(group -> {
            Integer groupId = group.getGroupId();
            List<VkPhoto> photosFromAlbum = vkPhotoRepository.findByGroupId(groupId);
            int size = photosFromAlbum.size();
            if (size > 0) {
                List<VkPhoto> randomPhotoList = new Random().ints(10, 0, size).boxed()
                        .map(photosFromAlbum::get)
                        .collect(Collectors.toList());

                vkService.sendPhotoToGroup(groupId, randomPhotoList.stream().map(VkPhoto::getVkImageId).collect(Collectors.joining(",")));
                vkPhotoRepository.deleteAll(randomPhotoList);
                Map<String, String> setting = group.getSetting();
                if (Objects.nonNull(setting) && "true".equals(setting.get("tg_duplicate_enabled"))) {
                    String tg_chat_id = setting.get("tg_chat_id");
                    tgBotService.sendPhotoesToChat(randomPhotoList, tg_chat_id);
                }
            }
        });
    }

}
