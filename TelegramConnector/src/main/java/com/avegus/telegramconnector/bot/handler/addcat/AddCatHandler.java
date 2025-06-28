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

// Asks photo
@Slf4j
@RequiredArgsConstructor
@Service
public class AddCatHandler implements MessageHandler {
    private final MessageSender messageSender;
    private final BotStateService stateService;

    public void handle(UpdateData update) {
        stateService.updateState(update.getUsername(), update.getUserId(), BotState.ADD_CAT_ASK_PHOTO);
        messageSender.sendMarkup(update.getUserId(), backButton(), Captions.REQUEST_PHOTO);
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.ADD_CAT;
    }
}
