package ru.zhendozzz.vkbot.enums.weather;

import java.util.Arrays;

public enum WeatherConditionEnum {
    CLEAR("clear", "ясно"),
    PARTLY_CLOUDY("partly-cloudy", "малооблачно"),
    CLOUDY("cloudy", "облачно с прояснениями"),
    OVERCAST("overcast", "пасмурно"),
    DRIZZLE("drizzle", "морось"),
    LIGHT_RAIN("light-rain", "небольшой дождь"),
    RAIN("rain ", "дождь"),
    MODERATE_RAIN("moderate-rain", "умеренно сильный дождь"),
    HEAVY_RAIN("heavy-rain", "сильный дождь"),
    CONTINUOUS_HEAVY_RAIN("continuous-heavy-rain", "длительный сильный дождь"),
    SHOWERS("showers", "ливень"),
    WET_SNOW("wet-snow", "дождь со снегом"),
    LIGHT_SNOW("light-snow", "небольшой снег"),
    SNOW("snow ", "снег"),
    SNOW_SHOWERS("snow-showers", "снегопад"),
    HAIL("hail", "град"),
    THUNDERSTORM("thunderstorm", "гроза"),
    THUNDERSTORM_WITH_RAIN("thunderstorm-with-rain", "дождь с грозой"),
    THUNDERSTORM_WITH_HAIL("thunderstorm-with-hail", "гроза с градом"),
    ;

    WeatherConditionEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;
    private String description;


    public static WeatherConditionEnum byName(String part_name) {
        return Arrays.stream(values()).filter(partName -> partName.name.equals(part_name)).findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
