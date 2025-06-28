package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.avegus.telegramconnector.factory.InlineKeyboardFactory.backButton;

// Sends name question
@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatAskNameHandler implements MessageHandler {

    private final MessageSender messageSender;
    private final BotStateService botStateService;

    @Override
    public void handle(UpdateData update) {
        messageSender.sendMarkup(update.getUserId(), backButton(), Captions.PHOTO_SAVED_ASK_NAME);
        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.ADD_CAT_CONSUME_NAME);
    }

    @Override
    public boolean canHandle(UpdateData update) {
        return update.getBotState() == BotState.ADD_CAT_ASK_NAME;
    }
}
