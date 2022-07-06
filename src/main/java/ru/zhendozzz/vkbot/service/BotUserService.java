package ru.zhendozzz.vkbot.service;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.BotUser;
import ru.zhendozzz.vkbot.dao.repository.BotUserRepository;

@Service
public class BotUserService {
    private final BotUserRepository botUserRepository;

    public BotUserService(BotUserRepository botUserRepository) {
        this.botUserRepository = botUserRepository;
    }

    public BotUser getVkUser(){
        Iterable<BotUser> all = botUserRepository.findAllByType("VK");
        return StreamSupport.stream(all.spliterator(), false)
            .findFirst().orElse(null);
    }

    public BotUser getTelegramUser(){
        Iterable<BotUser> all = botUserRepository.findAllByType("TG");
        return StreamSupport.stream(all.spliterator(), false)
            .findFirst().orElse(null);
    }
}
