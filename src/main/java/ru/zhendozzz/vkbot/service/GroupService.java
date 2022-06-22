package ru.zhendozzz.vkbot.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.repository.GroupRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getGroups() {
        Iterable<Group> all = groupRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
            .collect(Collectors.toList());
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

    public void save(Group foundGroup) {
        groupRepository.save(foundGroup);
    }

    public Optional<Group> findGroupByGroupId(Integer groupId) {
        return groupRepository.findGroupByGroupId(groupId);
    }
}
