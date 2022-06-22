package ru.zhendozzz.vkbot.service.weather;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dto.weather.Forecast;
import ru.zhendozzz.vkbot.dto.weather.ForecastPart;
import ru.zhendozzz.vkbot.dto.weather.Weather;
import ru.zhendozzz.vkbot.enums.weather.WeatherConditionEnum;
import ru.zhendozzz.vkbot.enums.weather.WeatherPartEnum;
import ru.zhendozzz.vkbot.enums.weather.WeatherWindEnum;

import java.util.*;

@Service
public class WeatherExplainerService {

    private Weather convertJSONtoWeather(String weatherFromResource) {
        Gson gson = new Gson();
        return gson.fromJson(weatherFromResource, Weather.class);
    }

    public String explainWeather(String weatherFromResource) {
        Weather weather = convertJSONtoWeather(weatherFromResource);
        Forecast forecast = weather.getForecast();
        Map<String, ForecastPart> parts = remapParts(forecast.getParts());
        StringBuilder res = new StringBuilder();
        res.append("По данным сервиса Яндекс.Погода\n").append("---------------------\n");
        parts.forEach((s, part) -> {
            res.append(WeatherPartEnum.byName(part.getPart_name()).getDescription()).append("\n");
            res.append(WeatherConditionEnum.byName(part.getCondition()).getDescription()).append(",");
            res.append("температура от ").append(part.getTemp_min()).append(" до ").append(part.getTemp_max()).append("\n");
            res.append("направление ветра ").append(WeatherWindEnum.byName(part.getWind_dir()).getDescription());
            res.append(", давление ").append(part.getPressure_mm()).append(" мм рт. ст.");
            res.append("\n").append("---------------------\n");
        });
        return res.toString();
    }


    private Map<String, ForecastPart> remapParts(ArrayList<ForecastPart> forecastParts) {
        Map<String, ForecastPart> parts = new TreeMap<>();
        for (ForecastPart forecastPart : forecastParts) {
            WeatherPartEnum weatherPartName = WeatherPartEnum.byName(forecastPart.getPart_name());
            parts.put(weatherPartName.name(), forecastPart);
        }
        return parts;
    }
}
