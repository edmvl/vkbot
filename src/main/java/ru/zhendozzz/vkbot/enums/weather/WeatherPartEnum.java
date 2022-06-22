package ru.zhendozzz.vkbot.enums.weather;

import java.util.Arrays;

public enum WeatherPartEnum {
    NIGHT("night", "ночь"),
    MORNING("morning", "утро"),
    DAY("day", "день"),
    EVENING("evening", "вечер");

    WeatherPartEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;
    private String description;


    public static WeatherPartEnum byName(String part_name) {
        return Arrays.stream(values()).filter(partName -> partName.name.equals(part_name)).findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
