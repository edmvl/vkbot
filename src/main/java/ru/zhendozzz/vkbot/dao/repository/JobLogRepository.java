package ru.zhendozzz.vkbot.dao.repository;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.zhendozzz.vkbot.dao.entity.JobLog;

@Repository
public interface JobLogRepository extends CrudRepository<JobLog, Long> {
    Optional<JobLog> findByGroupIdAndDateAndSuccess(Integer groupId, LocalDate date, Boolean success);
}
