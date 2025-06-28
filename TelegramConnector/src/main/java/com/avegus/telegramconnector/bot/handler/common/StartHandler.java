package com.avegus.telegramconnector.bot.handler.common;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.service.state.BotStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StartHandler implements MessageHandler {
    private final BotStateService botStateService;
    private final MessageSender messageSender;

    public void handle(UpdateData update) {

        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.ASK_NAME);
        messageSender.sendMessage(update.getUserId(), "Привет! Как тебя зовут?");
    }

    public boolean canHandle(UpdateData updateData) {
        return updateData.getBotState() == BotState.JOINED;
    }
}
