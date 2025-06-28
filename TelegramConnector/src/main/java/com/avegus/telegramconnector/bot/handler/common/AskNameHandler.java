package com.avegus.telegramconnector.bot.handler.common;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AskNameHandler implements MessageHandler {

    private final BotStateService botStateService;
    private final MessageSender messageSender;

    public void handle(UpdateData update) {

        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.MAIN_MENU);
        messageSender.sendMessage(update.getUserId(), String.format("Привет, %s!", update.getMessage().orElse("name-error")));
        messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.menuMarkup(), Captions.MENU_CAPTION);
    }

    public boolean canHandle(UpdateData update) {
        return update.hasMessage() && update.getBotState() == BotState.ASK_NAME;
    }
}
