package com.avegus.telegramconnector.bot.sender;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageSender {
    void sendMessage(Long userId, String text);
    void sendPhotoWithMarkup(Long userId, String caption, InlineKeyboardMarkup markup, String fileId);
    void sendMarkup(Long userId, InlineKeyboardMarkup markup, String caption);
}
