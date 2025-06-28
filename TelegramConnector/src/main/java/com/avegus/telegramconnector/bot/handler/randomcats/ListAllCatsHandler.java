package com.avegus.telegramconnector.bot.handler.randomcats;

import com.avegus.commons.model.UserIdDto;
import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListAllCatsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;

    public void handle(UpdateData update) {
        kafkaProducer.requestRandomCat(new UserIdDto(update.getUserId()));
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.LIST_ALL_CATS;
    }
}
