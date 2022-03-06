package ru.zhendozzz.vkbot.dto.joblog;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobLogListDto {
    List<JobLogDto> jobLogList;
}
