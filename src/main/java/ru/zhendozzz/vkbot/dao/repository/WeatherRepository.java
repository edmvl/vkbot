package ru.zhendozzz.vkbot.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhendozzz.vkbot.dao.entity.Weather;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    Optional<Weather> getByDateAndGroupId(LocalDate localDate, Integer groupId);
}
