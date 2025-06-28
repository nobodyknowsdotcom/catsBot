package com.avegus.telegramconnector.model.dto;

import com.avegus.telegramconnector.model.enums.BotState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallbackQueryData {
    // Telegram callbackData entered chat

    /**
     * State
     */
    private BotState s;
    /**
     * Payload
     */
    private String p;
}
