package ru.zhendozzz.vkbot.controller;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dto.common.CommonDto;
import ru.zhendozzz.vkbot.dto.group.GroupDto;
import ru.zhendozzz.vkbot.dto.group.GroupListDto;
import ru.zhendozzz.vkbot.service.GroupService;
import ru.zhendozzz.vkbot.service.VKService;
import ru.zhendozzz.vkbot.service.photoposter.PhotoService;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {
    private final GroupService groupService;
    private final PhotoService photoService;
    private final VKService vkService;
    public GroupController(GroupService groupService, PhotoService photoService, VKService vkService) {
        this.groupService = groupService;
        this.photoService = photoService;
        this.vkService = vkService;
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

    @GetMapping("/invite/{groupId}")
    public CommonDto invite(@PathVariable("groupId") Integer groupId) {
        vkService.inviteModerator(groupId);
        return CommonDto.builder()
            .result("Ok")
            .build();
    }

    @GetMapping("/loadphoto/{groupId}/{albumId}")
    public CommonDto loadPhoto(@PathVariable("groupId") Integer groupId, @PathVariable("albumId") String albumId) {
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
