package ru.zhendozzz.vkbot.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VkPhoto {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private Long groupId;
    private String vkImageId;
    private String url;
}
