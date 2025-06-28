package com.avegus.telegramconnector.bot.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.avegus.telegramconnector.bot.sender.MessageSenderHelper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSenderImpl implements MessageSender {

    private final TelegramClient telegramClient;

    @Override
    public void sendMessage(Long userId, String text) {
        log.debug("Sending text message to {}, contents: {}", userId, text);

        try {
            telegramClient.execute(toSendMessage(userId, text));
        } catch (Exception e) {
            log.error("Error while sending message with content {} to {}, ", text, userId, e);
        }
    }

    @Override
    public void sendPhotoWithMarkup(Long chatId, String caption, InlineKeyboardMarkup markup, String fileId) {
        try {
            telegramClient.execute(toSendPhotoWithMarkup(chatId, caption, markup, fileId));
        } catch (Exception e) {
            log.error("Error while sending photo with file {} to {}, ", fileId, chatId, e);
        }
    }

    @Override
    public void sendMarkup(Long userId, InlineKeyboardMarkup markup, String caption) {
        log.debug("Sending markup with text to {}, text {}", userId, caption);

        try {
            telegramClient.execute(toSendMarkupWithCaption(userId, markup, caption));
        } catch (Exception e) {
            log.error("Error while sending markup with text {} to {}, ", caption, userId, e);
        }
    }
}
