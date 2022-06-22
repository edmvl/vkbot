package ru.zhendozzz.vkbot.enums.weather;

import java.util.Arrays;

public enum WeatherWindEnum {
    NW("nw", "северо-западное"),
    N("n", "северное"),
    NE("ne", "северо-восточное"),
    E("e", "восточное"),
    SE("se", "юго-восточное"),
    S("s", "южное"),
    SW("sw", "юго-западное"),
    W("w", "западное"),
    C("c", "штиль"),
    ;

    WeatherWindEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;
    private String description;


    public static WeatherWindEnum byName(String part_name) {
        return Arrays.stream(values()).filter(partName -> partName.name.equals(part_name)).findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
