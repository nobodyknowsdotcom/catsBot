package com.avegus.telegramconnector.bot.handler.mycats;

import com.avegus.commons.model.CatIdWithUserId;
import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyCatsDeleteHandler implements MessageHandler {
    private final KafkaProducerService kafkaProducer;
    private final MessageSender messageSender;
    private final MyCatsHandler myCatsHandler;

    @SneakyThrows
    public void handle(UpdateData update) {
        var catId = update.getCallbackData().get().getP();

        if (catId != null && !catId.isEmpty()) {
            kafkaProducer.sendDeleteCatRequest(new CatIdWithUserId(catId, update.getUserId()));
            messageSender.sendMessage(update.getUserId(), Captions.DELETED_ACTION);
            myCatsHandler.handle(update);
        } else {
            log.error("Invalid callback query with cat id");
        }
    }

    public boolean canHandle(UpdateData update) {
        return update.hasCallbackData() && update.getBotState() == BotState.DELETE;
    }
}
