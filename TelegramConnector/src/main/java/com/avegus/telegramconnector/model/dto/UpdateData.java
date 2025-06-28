package com.avegus.telegramconnector.model.dto;

import com.avegus.telegramconnector.model.enums.BotState;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import java.util.List;
import java.util.Optional;

@Data
public class UpdateData {
    private Long userId;
    private String username;
    private BotState botState;

    private Optional<String> message = Optional.empty();
    private Optional<CallbackQueryData> callbackData = Optional.empty();
    private Optional<List<PhotoSize>> photoSizes = Optional.empty();

    public boolean hasMessage() {
        return message.isPresent() && !message.get().isEmpty();
    }

    public boolean hasCallbackData() {
        return callbackData.isPresent();
    }

    public boolean hasPhotoSizes() {
        return photoSizes.isPresent() && !photoSizes.get().isEmpty();
    }

    public void setCallBackDataAndBotState(Optional<CallbackQueryData> callbackData) {
        this.callbackData = callbackData;

        callbackData.ifPresent(callbackQueryData -> this.botState = callbackQueryData.getS());
    }
}
