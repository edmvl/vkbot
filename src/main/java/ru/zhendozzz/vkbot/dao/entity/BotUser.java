package ru.zhendozzz.vkbot.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class BotUser {
    @Id
    private Long id;

    private Integer vkUserId;

    private String token;
}
