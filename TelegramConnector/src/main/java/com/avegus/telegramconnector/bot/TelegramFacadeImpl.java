package com.avegus.telegramconnector.bot;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.service.state.BotStateService;
import com.avegus.telegramconnector.model.enums.BotState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramFacadeImpl implements TelegramFacade {

    private final BotStateService botStateService;
    private final List<MessageHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void handleUpdate(Update update) {

        var updateData = toUpdateData(update);
        for (MessageHandler handler : handlers) {

            if (handler.canHandle(updateData)) {
                handler.handle(updateData);
            }
        }
    }

    /**
     * Fill data for handlers. BotState may come in update, may be taken from DB, depend on current case
     */
    private UpdateData toUpdateData(Update update) {
        var updateData = new UpdateData();

        if (!update.hasMessage() && !update.hasCallbackQuery()) {
            updateData.setBotState(BotState.UNKNOWN);
            return updateData;
        }

        // If request data is enough, return it
        extractUpdate2Data(update, updateData);
        if (updateData.getBotState() != null) {
            return updateData;
        }

        // Else fetch status from db
        var maybeStateFromDb = botStateService.getCurrentState(updateData.getUserId());

        if (maybeStateFromDb.isEmpty() || Objects.equals(updateData.getMessage().orElse(""), "/start")) {
            updateData.setBotState(BotState.JOINED);
            botStateService.updateState(updateData.getUsername(), updateData.getUserId(), updateData.getBotState());
        } else {
            updateData.setBotState(maybeStateFromDb.get());
        }

        if (updateData.getBotState() == null) {
            updateData.setBotState(BotState.UNKNOWN);
        }

        return updateData;
    }

    private void extractUpdate2Data(Update extractFrom, UpdateData extractTo) {
        if (extractFrom.hasCallbackQuery()) {
            extractTo.setUsername(extractFrom.getCallbackQuery().getFrom().getUserName());
            extractTo.setUserId(extractFrom.getCallbackQuery().getFrom().getId());
            extractTo.setCallBackDataAndBotState(tryGetCallbackData(extractFrom.getCallbackQuery()));
        }

        if (extractFrom.hasMessage()) {
            extractTo.setUsername(extractFrom.getMessage().getFrom().getUserName());
            extractTo.setUserId(extractFrom.getMessage().getFrom().getId());

            if (extractFrom.getMessage().hasText()) {
                extractTo.setMessage(Optional.of(extractFrom.getMessage().getText()));
            }

            if (extractFrom.getMessage().hasPhoto()) {
                extractTo.setPhotoSizes(Optional.of(extractFrom.getMessage().getPhoto()));
            }
        }
    }

    private Optional<CallbackQueryData> tryGetCallbackData(CallbackQuery callbackQuery) {
        try {
            return Optional.of(objectMapper.readValue(callbackQuery.getData(), CallbackQueryData.class));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
