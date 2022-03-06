package ru.zhendozzz.vkbot.dto.botuser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotUserListDto {
    private List<BotUserDto> botUserList;
}
