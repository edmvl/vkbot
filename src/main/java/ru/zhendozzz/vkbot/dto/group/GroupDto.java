package ru.zhendozzz.vkbot.dto.group;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private Long id;

    private Long groupId;

    private String groupName;

    private Map<String, String> setting;
}
