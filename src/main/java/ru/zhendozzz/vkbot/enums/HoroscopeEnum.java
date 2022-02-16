package ru.zhendozzz.vkbot.enums;

import java.util.Arrays;

public enum HoroscopeEnum {
    ARIES("Овен", "aries"),
    TAURUS("Телец", "taurus"),
    GEMINI("Близнецы", "gemini"),
    CANCER("Рак", "cancer"),
    LEO("Лев", "leo"),
    VIRGO("Дева", "virgo"),
    LIBRA("Весы", "libra"),
    SCORPIO("Скорпион", "scorpio"),
    SAGITTARIUS("Стрелец", "sagittarius"),
    CAPICORN("Козерог", "capricorn"),
    AQUARIUS("Водолей", "aquarius"),
    PISCES("Рыбы", "pisces");

    HoroscopeEnum(String name, String sysname) {
        this.name = name;
        this.sysname = sysname;
    }

    private final String name;
    private final String sysname;

    public String getName() {
        return name;
    }

    public String getSysname() {
        return sysname;
    }

    public static HoroscopeEnum bySysname(String sysname) {
        return Arrays.stream(values())
            .filter(horoscopeEnum -> horoscopeEnum.sysname.equals(sysname))
            .findFirst()
            .orElseThrow();
    }
}
