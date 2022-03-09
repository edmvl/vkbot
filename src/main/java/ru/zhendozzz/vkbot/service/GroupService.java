package ru.zhendozzz.vkbot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.repository.GroupRepository;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final BotUserService botUserService;
    private final VKService vkService;

    public GroupService(GroupRepository groupRepository, BotUserService botUserService, VKService vkService) {
        this.groupRepository = groupRepository;
        this.botUserService = botUserService;
        this.vkService = vkService;
    }

    public List<Group> getGroups() {
        Iterable<Group> all = groupRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
            .collect(Collectors.toList());
    }

    public String sendHoro(Integer groupId){
        BotUser user = botUserService.getUser();
        return vkService.sendHoroGroup(groupId, user.getToken(), user.getVkUserId());
    }

    public String sendHoroToGroups(){
        BotUser user = botUserService.getUser();
        List<Group> groups = getGroups();
        groups.forEach(botUser -> vkService.sendHoroGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
        return "Ok";
    }

    public void congratsAllGroups(){
        BotUser user = botUserService.getUser();
        List<Group> groups = getGroups();
        groups.forEach(botUser -> vkService.congratsGroup(botUser.getGroupId(), user.getToken(), user.getVkUserId()));
    }

    public void congratsGroup(Integer groupId){
        BotUser user = botUserService.getUser();
        vkService.congratsGroup(groupId, user.getToken(), user.getVkUserId());
    }

    public Group save(Integer groupId, String groupName) {
        Group group = Group.builder()
            .groupId(groupId)
            .groupName(groupName)
            .setting(new HashMap<>())
            .build();
        return groupRepository.save(group);
    }

    @SneakyThrows
    public Group update(Long id, Map<String, String> setting) {
        Optional<Group> byId = groupRepository.findById(id);
        if (byId.isPresent()) {
            Group foundGroup = byId.get();
            foundGroup.setSetting(setting);
            return groupRepository.save(foundGroup);
        } else {
            throw new Exception();
        }
    }

    @SneakyThrows
    public String inviteModerator(Integer groupId){
        BotUser user = botUserService.getUser();
        String s = vkService.inviteModerator(groupId, user.getToken(), user.getVkUserId());
        Optional<Group> byId = groupRepository.findGroupByGroupId(groupId);
        if (byId.isPresent()) {
            Group foundGroup = byId.get();
            Map<String, String> setting = foundGroup.getSetting();
            setting.put("moderInvited", "true");
            groupRepository.save(foundGroup);
        } else {
            throw new Exception();
        }
        return s;
    }
}
