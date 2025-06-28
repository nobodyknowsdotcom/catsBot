package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.commons.model.CatIdWithUserId;
import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyCatsActionsHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;

    @SneakyThrows
    public void handle(UpdateData update) {
        var catId = update.getCallbackData().get().getP();

        if (catId != null && !catId.isEmpty()) {
            kafkaProducer.requestUserCat(new CatIdWithUserId(catId, update.getUserId()));
        } else {
            log.error("Invalid callback query with cat id");
        }
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.MANAGE_CAT;
    }
}
