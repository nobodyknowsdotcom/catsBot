package com.avegus.telegramconnector.bot.state;

import com.avegus.telegramconnector.bot.state.enums.BotState;

/**
 * Manages User state
 */
public interface BotStateService {
    void updateState(String userId, BotState newState);
    BotState getCurrentState(String userId);
}
