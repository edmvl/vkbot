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
public class Horo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String sign;
    private String text;
    private LocalDate date;
}
