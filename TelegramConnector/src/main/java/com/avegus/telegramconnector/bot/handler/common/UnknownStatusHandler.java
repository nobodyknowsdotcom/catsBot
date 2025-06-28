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
public class UnknownStatusHandler implements MessageHandler {

    private final BotStateService botStateService;
    private final MessageSender messageSender;

    @Override
    public void handle(UpdateData update) {
        log.info("Unknown status update received from {}", update.getUserId());
        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.MAIN_MENU);
        messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.menuMarkup(), Captions.MENU_CAPTION);
    }

    @Override
    public boolean canHandle(UpdateData update) {
        return update.getBotState() == BotState.UNKNOWN;
    }
}
