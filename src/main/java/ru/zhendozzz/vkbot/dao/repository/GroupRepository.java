package ru.zhendozzz.vkbot.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.zhendozzz.vkbot.dao.entity.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> findGroupByGroupId(Integer aLong);
}
