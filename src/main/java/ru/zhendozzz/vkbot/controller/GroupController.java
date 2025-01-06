package ru.zhendozzz.vkbot.controller;

import org.springframework.web.bind.annotation.*;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dto.common.CommonDto;
import ru.zhendozzz.vkbot.dto.group.GroupDto;
import ru.zhendozzz.vkbot.dto.group.GroupListDto;
import ru.zhendozzz.vkbot.service.group.GroupService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService groupService;
    private final PhotoService photoService;

    public GroupController(GroupService groupService, PhotoService photoService) {
        this.groupService = groupService;
        this.photoService = photoService;
    }

    @GetMapping("/list")
    public GroupListDto getAllGroup(){
        return GroupListDto.builder()
            .settingList(
                groupService.getGroups().stream().map(
                    groupSettings -> GroupDto.builder()
                        .id(groupSettings.getId())
                        .groupId(groupSettings.getGroupId())
                        .groupName(groupSettings.getGroupName())
                        .setting(groupSettings.getSetting())
                        .build()
                ).collect(Collectors.toList())
            )
            .build();
    }

    @PostMapping("/")
    public GroupDto saveGroup(@RequestBody GroupDto groupDto){
        Group group = groupService.save(
            groupDto.getGroupId(),
            groupDto.getGroupName()
        );
        return GroupDto.builder()
            .id(group.getId())
            .groupId(group.getGroupId())
            .groupName(group.getGroupName())
            .build();
    }

    @PatchMapping("/setting")
    public GroupDto updateGroup(@RequestBody GroupDto groupDto){
        Group group = groupService.update(
            groupDto.getId(),
            groupDto.getSetting()
        );
        return GroupDto.builder()
            .id(group.getId())
            .groupId(group.getGroupId())
            .groupName(group.getGroupName())
            .setting(group.getSetting())
            .build();
    }

    @GetMapping("/loadphoto/{groupId}/{albumId}")
    public CommonDto loadPhoto(@PathVariable("groupId") Long groupId, @PathVariable("albumId") String albumId) {
        photoService.loadPhotoListFromAlbum(albumId, groupId);
        return CommonDto.builder()
            .result("Ok")
            .build();
    }

    @GetMapping("/sendphoto")
    public CommonDto sendPhoto() {
        photoService.sendPhotoToGroups();
        return CommonDto.builder()
            .result("Ok")
            .build();
    }


}
