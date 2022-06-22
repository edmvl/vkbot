package ru.zhendozzz.vkbot.dto.weather;

import lombok.Data;

@Data
public class Fact {
    private Long obs_time;
    private Integer temp;
    private Integer feels_like;
}