package ru.zhendozzz.vkbot.service.weather;

import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.entity.Weather;
import ru.zhendozzz.vkbot.dao.repository.WeatherRepository;
import ru.zhendozzz.vkbot.service.GroupService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class WeatherService {
    private final GroupService groupService;
    private final WeatherLoaderService weatherLoaderService;
    private final WeatherRepository weatherRepository;
    private final WeatherExplainerService weatherExplainerService;

    public WeatherService(GroupService groupService, WeatherLoaderService weatherLoaderService, WeatherRepository weatherRepository, WeatherExplainerService weatherExplainerService) {
        this.groupService = groupService;
        this.weatherLoaderService = weatherLoaderService;
        this.weatherRepository = weatherRepository;
        this.weatherExplainerService = weatherExplainerService;
    }

    public void downloadWeather() {
        List<Group> groups = groupService.getGroups();
        groups.forEach(group -> {
            Map<String, String> setting = group.getSetting();
            if (Objects.nonNull(setting) && "true".equals(setting.get("weather_enabled"))) {
                String weatherCoord = setting.get("weather_coord");
                String[] s = weatherCoord.split(" ");
                String lat = s[0];
                String lon = s[1];
                String weatherObject = weatherLoaderService.getWeatherObject(lat, lon);
                LocalDate now = LocalDate.now();
                weatherRepository.save(Weather.builder()
                        .groupId(group.getGroupId())
                        .date(now)
                        .text(weatherObject)
                        .part(getWeatherPart(now, group.getGroupId()) + 1)
                        .build());
            }
        });
    }

    public String getWeather(LocalDate date, Integer groupId) {
        Integer weatherPart = getWeatherPart(date, groupId);
        Optional<Weather> byDateAndGroupId = weatherRepository.getByDateAndGroupIdAndPart(date, groupId, weatherPart);
        return weatherExplainerService.explainWeather(byDateAndGroupId.orElse(new Weather()).getText());
    }

    private Integer getWeatherPart(LocalDate date, Integer groupId) {
        Integer part = 0;
        List<Weather> weathers = weatherRepository.getByDateAndGroupIdOrderByPartDesc(date, groupId);
        Optional<Weather> first = weathers.stream().findFirst();
        if (first.isPresent()) {
            part = first.get().getPart();
        }
        return part;
    }
}
