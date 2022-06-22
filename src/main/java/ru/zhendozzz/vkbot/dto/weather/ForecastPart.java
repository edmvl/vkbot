package ru.zhendozzz.vkbot.dto.weather;

import lombok.Data;

@Data
public class ForecastPart {
    private String part_name;
    private Integer temp_min;
    private Integer temp_avg;
    private Integer temp_max;
    private Float wind_speed;
    private Float wind_gust;
    private String wind_dir;
    private Integer pressure_mm;
    private Integer pressure_pa;
    private Integer humidity;
    private Float prec_mm;
    private Float prec_prob;
    private Float prec_period;
    private String condition;
    private Integer feels_like;
}
