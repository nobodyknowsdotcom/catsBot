package com.avegus.telegramconnector.service.state;

import com.avegus.telegramconnector.model.enums.BotState;

import java.util.Optional;

/**
 * Manages User state
 */
public interface BotStateService {
    void updateState(String username, Long userId, BotState botState);
    Optional<BotState> getCurrentState(Long userId);
}
