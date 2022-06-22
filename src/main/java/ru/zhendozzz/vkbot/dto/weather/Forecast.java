package ru.zhendozzz.vkbot.dto.weather;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Forecast {
    private String date;
    private Long date_ts;
    private Integer week;
    private String sunrise;
    private String sunset;
    private Integer moon_code;
    private ArrayList<ForecastPart> parts;
}
