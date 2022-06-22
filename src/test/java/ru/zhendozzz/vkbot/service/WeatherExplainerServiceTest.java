package ru.zhendozzz.vkbot.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zhendozzz.vkbot.service.weather.WeatherExplainerService;
import ru.zhendozzz.vkbot.service.weather.WeatherLoaderService;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherExplainerServiceTest {

    @Mock
    private WeatherLoaderService weatherService;

    @InjectMocks
    private WeatherExplainerService weatherExplainerService;

    @SneakyThrows
    @Test
    @DisplayName("Расшифровка погоды из json")
    void getWeather(){
        String weatherResponce = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("weatherResponce.json")).readAllBytes());
        when(weatherService.getWeatherObject(any(), any())).thenReturn(weatherResponce);
        String weatherObject = weatherService.getWeatherObject("", "");
        String weather = weatherExplainerService.explainWeather(weatherObject);
        Assertions.assertNotNull(weather);
        Assertions.assertEquals("По данным сервиса Яндекс.Погода\n" +
                "---------------------\n"+
                "день\n" +
                "ливень,температура от 12 до 13\n" +
                "направление ветра юго-западное, давление 745 мм рт. ст.\n" +
                "---------------------\n"+
                "вечер\n" +
                "небольшой дождь,температура от 9 до 13\n" +
                "направление ветра юго-восточное, давление 745 мм рт. ст.\n"+
                "---------------------\n"
                , weather);
    }
}