package ru.zhendozzz.vkbot.dao.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.zhendozzz.vkbot.dao.entity.Horo;

@Repository
public interface HoroRepository extends CrudRepository<Horo, Long> {
    Optional<Horo> getAllByDateAndSign(LocalDate localDate, String sign);
}
