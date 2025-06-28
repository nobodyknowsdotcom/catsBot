package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.commons.model.AddCatRequest;
import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.broker.KafkaProducerService;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.avegus.telegramconnector.factory.InlineKeyboardFactory.backButton;

// Consumes name, after name asking
@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatNameHandler implements MessageHandler {

    private final KafkaProducerService kafkaProducerService;
    private final MessageSender messageSender;
    private final BotStateService botStateService;
    private final InMemStorageById storageById;

    public void handle(UpdateData update) {

        var fileId = storageById.get(update.getUserId());
        if (fileId.isEmpty()) {
            botStateService.updateState(update.getUsername(), update.getUserId(), BotState.ADD_CAT_ASK_PHOTO);
            messageSender.sendMarkup(update.getUserId(), backButton(), Captions.WEIRD_PHOTO);
            return;
        }

        var catName = update.getMessage().get();
        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.MAIN_MENU);
        messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.menuMarkup(), String.format(Captions.CAT_SAVED, catName));

        var addCatRequest = new AddCatRequest(update.getUserId(), update.getUsername(), catName, fileId.get());
        kafkaProducerService.sendAddCatRequest(addCatRequest);
    }

    public boolean canHandle(UpdateData update) {
        return update.hasMessage() && update.getBotState() == BotState.ADD_CAT_CONSUME_NAME;
    }
}
