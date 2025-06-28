package com.avegus.telegramconnector.bot.handler.randomcats;

import com.avegus.commons.model.CatIdDto;
import com.avegus.commons.model.CatIdWithUserId;
import com.avegus.commons.model.UserIdDto;
import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Rating;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatRatingHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;

    @SneakyThrows
    public void handle(UpdateData update) {

        String catId;
        Rating catRate;
        try {
            var payload = update.getCallbackData().get().getP();
            catId = payload.split(":")[0];
            catRate = Rating.valueOf(payload.split(":")[1]);

            if (catId == null || catId.isEmpty()) {
                log.error("Empty catId in CatRatingHandler");
                return;
            }
        } catch (Exception e) {
            log.error("Cannot parse data in CatRatingHandler, ", e);
            return;
        }

        switch (catRate) {
            case YES:
                kafkaProducer.sendLikeEvent(new CatIdWithUserId(catId, update.getUserId()));
                break;
            case NO:
                kafkaProducer.sendDislikeEvent(new CatIdWithUserId(catId, update.getUserId()));
                break;
            default:
                log.warn("Received a callback query with invalid rating");
        }
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.RATE;
    }
}
