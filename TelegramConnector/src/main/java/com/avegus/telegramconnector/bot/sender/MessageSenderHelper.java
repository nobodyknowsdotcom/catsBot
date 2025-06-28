package com.avegus.telegramconnector.bot.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageSenderHelper {
    public static SendMessage toSendMessage(Long userId, String text) {
        return SendMessage.builder()
                .chatId(String.valueOf(userId))
                .text(text)
                .build();
    }

    public static File byteArrayToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }

    public static SendMessage toSendMarkupWithCaption(Long userId, InlineKeyboardMarkup markup, String caption) {
        var msg = toSendMessage(userId, caption);
        msg.setReplyMarkup(markup);
        return msg;
    }

    public static SendPhoto toSendPhotoWithMarkup(Long chatId, String caption, InlineKeyboardMarkup markup,  String fileId) {
        return SendPhoto.builder()
                .chatId(chatId.toString())
                .photo(new InputFile(fileId))
                .replyMarkup(markup)
                .caption(caption)
                .build();
    }
}
