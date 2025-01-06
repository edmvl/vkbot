package ru.zhendozzz.vkbot.service.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.vk.api.sdk.objects.users.UserFull;
import org.springframework.stereotype.Service;

@Service
public class TextFormatterService {
    public String getTextForBirthDay(List<UserFull> members){

        String collect = members.stream().map(
            u -> "@id" + u.getId() + " (" +u.getFirstName() + " " + u.getLastName() + ")"
        ).collect(Collectors.joining(", "));

        boolean multipleMembers = members.size() > 1;
        String ending1 = multipleMembers ?  "ют" : "ет";
        String ending2 = multipleMembers ?  "ов" : "а";

        return "Сегодня праздну" + ending1 + " день рождения " + collect + " От всей души поздравляем именинник" + ending2;
    }
}
