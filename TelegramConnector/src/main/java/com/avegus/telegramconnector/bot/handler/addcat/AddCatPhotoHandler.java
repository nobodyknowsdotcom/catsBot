package com.avegus.telegramconnector.bot.handler.addcat;

import com.avegus.telegramconnector.bot.handler.MessageHandler;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.model.dto.UpdateData;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Captions;
import com.avegus.telegramconnector.service.state.BotStateService;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import static com.avegus.telegramconnector.factory.InlineKeyboardFactory.photoUploadDesision;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddCatPhotoHandler implements MessageHandler, InMemStorageById {

    private final MessageSender messageSender;
    private final BotStateService botStateService;
    private final ConcurrentHashMap<Long, String> userTempCatPhotos = new ConcurrentHashMap<>();

    public void handle(UpdateData update) {

        if (!update.hasPhotoSizes()) {
            messageSender.sendMarkup(update.getUserId(), InlineKeyboardFactory.menuMarkup(), Captions.WEIRD_PHOTO);
        }

        var photo = update.getPhotoSizes().get().stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .get();

        put(update.getUserId(), photo.getFileId());
        messageSender.sendMarkup(update.getUserId(), photoUploadDesision(), Captions.PHOTO_UPLOAD_DECISION);
        botStateService.updateState(update.getUsername(), update.getUserId(), BotState.ADD_PHOTO_MENU);
    }

    public boolean canHandle(UpdateData update) {
        return update.getBotState() == BotState.ADD_CAT_ASK_PHOTO;
    }

    public Optional<String> get(Long key) {
        return userTempCatPhotos.containsKey(key) ?
                Optional.of(userTempCatPhotos.get(key)) : Optional.empty();
    }

    public void put(Long key, String value) {
        userTempCatPhotos.put(key, value);
    }
}
