package ru.zhendozzz.vkbot.dto.joblog;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobLogDto {

    private Long id;

    private Integer groupId;

    private LocalDate date;

    private String text;

    private String type;

    private Boolean success;
}
