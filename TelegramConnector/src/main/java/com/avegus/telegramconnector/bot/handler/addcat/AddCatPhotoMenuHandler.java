package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.avegus.telegramconnector.factory.InlineKeyboardFactory.photoUploadDesision;

// Sends markup to decide what to do with uploaded photo
@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatPhotoMenuHandler implements MessageHandler {

    private final MessageSender messageSender;

    @Override
    public void handle(UpdateData update) {
        messageSender.sendMarkup(update.getUserId(), photoUploadDesision(), Captions.PHOTO_UPLOAD_DECISION_RETRY);
    }

    @Override
    public boolean canHandle(UpdateData update) {
        return update.getBotState() == BotState.ADD_PHOTO_MENU;
    }
}
