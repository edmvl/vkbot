package ru.zhendozzz.vkbot.service.photoposter;

import com.vk.api.sdk.objects.photos.Photo;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.entity.VkPhoto;
import ru.zhendozzz.vkbot.dao.repository.VkPhotoRepository;
import ru.zhendozzz.vkbot.service.GroupService;
import ru.zhendozzz.vkbot.service.VKService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final VKService vkService;
    private final GroupService groupService;
    private final VkPhotoRepository vkPhotoRepository;

    public PhotoService(VKService vkService, GroupService groupService, VkPhotoRepository vkPhotoRepository) {
        this.vkService = vkService;
        this.groupService = groupService;
        this.vkPhotoRepository = vkPhotoRepository;
    }


    public void loadPhotoListFromAlbum(String albumId, Integer groupId) {
        List<Photo> photosFromAlbum = vkService.getPhotosFromAlbum(albumId, groupId);
        List<VkPhoto> vkPhotoList = photosFromAlbum.stream().map(
                photo -> VkPhoto.builder()
                        .vkImageId("photo" + photo.getOwnerId() + "_" + photo.getId().toString())
                        .groupId(groupId)
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
            }
        });
    }

}
