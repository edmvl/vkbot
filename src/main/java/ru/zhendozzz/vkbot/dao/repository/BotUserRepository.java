package ru.zhendozzz.vkbot.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.zhendozzz.vkbot.dao.entity.BotUser;

@Repository
public interface BotUserRepository extends CrudRepository<BotUser, Long> {
}
