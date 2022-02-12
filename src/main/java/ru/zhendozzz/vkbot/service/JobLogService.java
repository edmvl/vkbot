package ru.zhendozzz.vkbot.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.zhendozzz.vkbot.dao.entity.JobLog;
import ru.zhendozzz.vkbot.dao.repository.JobLogRepository;

@Service
public class JobLogService {
    private final JobLogRepository jobLogRepository;

    public JobLogService(JobLogRepository jobLogRepository) {
        this.jobLogRepository = jobLogRepository;
    }

    public boolean isGroupProcessed(Integer groupId, LocalDate date) {
        Optional<JobLog> byIdAndDate = jobLogRepository.findByGroupIdAndDateAndSuccess(groupId, date, true);
        return byIdAndDate.isPresent();
    }

    public void saveLog(Integer groupId, LocalDate now, String text, boolean success){
        JobLog log = JobLog.builder()
            .groupId(groupId)
            .date(now)
            .text(text)
            .success(success)
            .build();
        jobLogRepository.save(log);
    }
}
