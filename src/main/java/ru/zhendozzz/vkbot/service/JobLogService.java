package ru.zhendozzz.vkbot.service;

import java.time.LocalDate;
import java.util.List;
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

    public boolean isGroupNotProcessed(Integer groupId, LocalDate date, String type) {
        Optional<JobLog> byIdAndDate = jobLogRepository.findByGroupIdAndDateAndSuccessAndType(groupId, date, true, type);
        return byIdAndDate.isEmpty();
    }

    public void saveLog(Integer groupId, LocalDate now, String text, boolean success, String type){
        JobLog log = JobLog.builder()
            .groupId(groupId)
            .date(now)
            .text(text)
            .type(type)
            .success(success)
            .build();
        jobLogRepository.save(log);
    }

    public List<JobLog> getAllLog(){
        return jobLogRepository.findAll();
    }
}
