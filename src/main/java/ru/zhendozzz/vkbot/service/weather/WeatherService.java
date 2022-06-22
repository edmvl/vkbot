package ru.zhendozzz.vkbot.service.weather;

import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Group;
import ru.zhendozzz.vkbot.dao.entity.Weather;
import ru.zhendozzz.vkbot.dao.repository.WeatherRepository;
import ru.zhendozzz.vkbot.service.GroupService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    public void downloadWeather(){
        List<Group> groups = groupService.getGroups();
        groups.forEach(group -> {
            Map<String, String> setting = group.getSetting();
            String weatherEnabled = setting.get("weather_enabled");
            if ("true".equals(weatherEnabled)){
                String weatherCoord = setting.get("weather_coord");
                String[] s = weatherCoord.split(" ");
                String lat = s[0];
                String lon = s[1];
                String weatherObject = weatherLoaderService.getWeatherObject(lat, lon);
                weatherRepository.save(Weather.builder()
                        .groupId(group.getGroupId())
                        .date(LocalDate.now())
                        .text(weatherObject)
                        .build());
            }
        });
    }

    public String getWeather(LocalDate date, Integer groupId){
        Optional<Weather> byDateAndGroupId = weatherRepository.getByDateAndGroupId(date, groupId);
        return weatherExplainerService.explainWeather(byDateAndGroupId.orElse(new Weather()).getText());
    }
}
