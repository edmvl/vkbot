package ru.zhendozzz.vkbot.enums;

public enum JobType {
    HORO_SEND("Отправка гороскопа в группе вк", "horo_send"),
    HORO_LOAD("Загрузка гороскопа из ресурса", "horo_load"),
    WEATHER_SEND("Отправка прогноза погоды в группе вк", "weather_send"),
    WEATHER_LOAD("Загрузка прогноза погоды из ресурса", "weather_load"),
    VK_CONGRATS("Поздравление в группе вк", "vk_congrats");

    private final String name;
    private final String sysName;

    JobType(String name, String sysName) {
        this.name = name;
        this.sysName = sysName;
    }

    public String getName() {
        return name;
    }

    public String getSysName() {
        return sysName;
    }
}
