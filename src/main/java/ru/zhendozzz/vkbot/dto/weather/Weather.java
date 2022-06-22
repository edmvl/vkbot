package ru.zhendozzz.vkbot.dto.weather;

import lombok.Data;

@Data
public class Weather {
    private Long now;
    private String now_dt;
    private Info info;
    private Fact fact;
    private Forecast forecast;
}


