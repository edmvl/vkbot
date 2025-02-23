package ru.zhendozzz.vkbot.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class BotUser {
    @Id
    private Long id;

    private Long vkUserId;

    private String token;

    private String type;
}
