package ru.zhendozzz.vkbot.dao.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.zhendozzz.vkbot.dao.entity.JobLog;

@Repository
public interface JobLogRepository extends CrudRepository<JobLog, Long> {
    Optional<JobLog> findByGroupIdAndDateAndSuccessAndType(Long groupId, LocalDate date, Boolean success, String type);
    List<JobLog> findAll();
}
