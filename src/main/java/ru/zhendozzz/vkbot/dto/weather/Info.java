package ru.zhendozzz.vkbot.dto.weather;

import lombok.Data;

@Data
public class Info {
    private String url;
    private String lat;
    private String lon;
}