package com.avegus.telegramconnector.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TelegramClientConfig {

    private final BotConfig config;

    @Bean
    public TelegramClient telegramClient() {

        if (config.getToken() == null || config.getToken().isEmpty()) {
            log.error("!!!\nBot token is empty\n!!!");
        } else {
            log.info("Bot token found and set");
        }

        return new OkHttpTelegramClient(config.getToken());
    }
}
