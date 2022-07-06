package ru.zhendozzz.vkbot.service.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.zhendozzz.vkbot.dao.entity.VkPhoto;
import ru.zhendozzz.vkbot.service.BotUserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TgBotService extends TelegramLongPollingBot {

    private final BotUserService botUserService;


    public TgBotService(BotUserService botUserService) {
        this.botUserService = botUserService;
    }

    @Override
    public String getBotUsername() {
        return "vkdoublerbot";
    }

    @Override
    public String getBotToken() {
        return botUserService.getTelegramUser().getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @SneakyThrows
    public void sendPhotoesToChat(List<VkPhoto> fileUrls, String chatId) {
        SendMediaGroup sendMediaGroup =  new SendMediaGroup();
        List<InputMedia> collect = fileUrls.stream().map(vkPhoto -> new InputMediaPhoto(vkPhoto.getUrl())).collect(Collectors.toList());
        sendMediaGroup.setMedias(collect);
        sendMediaGroup.setChatId(chatId);
        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
