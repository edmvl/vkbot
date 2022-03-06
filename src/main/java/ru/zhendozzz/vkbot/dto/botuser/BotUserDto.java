package ru.zhendozzz.vkbot.dto.botuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotUserDto {
    private Long id;

    private Integer vkUserId;

    private String token;

    private Integer groupId;
}
