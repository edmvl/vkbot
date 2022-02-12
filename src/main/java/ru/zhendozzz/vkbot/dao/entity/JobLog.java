package ru.zhendozzz.vkbot.dao.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobLog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Integer groupId;

    private LocalDate date;

    private String text;

    private Boolean success;
}
