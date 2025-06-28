package com.avegus.telegramconnector.bot.handler.addcat;

import java.util.Optional;

public interface InMemStorageById {
    Optional<String> get(Long key);
    void put(Long key, String value);
}
