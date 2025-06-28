package com.avegus.telegramconnector.bot.handler;

import com.avegus.telegramconnector.model.dto.UpdateData;

public interface MessageHandler {
    void handle(UpdateData update);
    boolean canHandle(UpdateData update);
}
