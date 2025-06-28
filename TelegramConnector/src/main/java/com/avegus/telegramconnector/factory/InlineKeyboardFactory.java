package com.avegus.telegramconnector.factory;

import com.avegus.commons.model.CatDto;
import com.avegus.telegramconnector.model.dto.CallbackQueryData;
import com.avegus.telegramconnector.model.enums.BotState;
import com.avegus.telegramconnector.model.enums.Rating;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InlineKeyboardFactory {

    private static final Logger log =  java.util.logging.Logger.getLogger(InlineKeyboardFactory.class.getName());
    private static final ObjectMapper om = new ObjectMapper();

    @SneakyThrows
    public static InlineKeyboardMarkup catRatingMarkup(String catId, Long likes, Long dislikes) {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text(String.format("\uD83D\uDC4D (%s)", likes.toString()))
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.RATE, catId + ":" + Rating.YES.name())
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text(String.format("\uD83D\uDC4E (%s)", dislikes.toString()))
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.RATE, catId + ":" + Rating.NO.name())
                        )
                )
                .build());


        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup menuMarkup() {
        var row1 = new InlineKeyboardRow();
        row1.add(InlineKeyboardButton.builder()
                .text("Мои котики")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MY_CATS, null)
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Смотреть котиков")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.LIST_ALL_CATS, null)
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Добавить котика")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.ADD_CAT, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup myCatCardMarkup(String catId) {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text("Удалить")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.DELETE, catId)
                        )
                )
                .build());

        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup myCatsMarkup(List<CatDto> catDtos) {
        var catRows = new ArrayList<InlineKeyboardRow>();
        var backBtnRow = new InlineKeyboardRow();

        // Coz we can have only 3 rows of cats and 1 leave for back button
        var batched = Lists.partition(catDtos, 3)
                .stream()
                .limit(3)
                .toList();
        batched.forEach(cats -> {
            var newRow = new InlineKeyboardRow();
            writeCats2Row(cats, newRow);
            catRows.add(newRow);
        });

        backBtnRow.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        catRows.add(backBtnRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(catRows)
                .build();
    }

    private static void writeCats2Row(List<CatDto> catDtos, InlineKeyboardRow row1) {
        catDtos.forEach(catDto -> {
            try {
                row1.add(InlineKeyboardButton.builder()
                        .text(catDto.getName())
                        .callbackData(
                                om.writeValueAsString(
                                        new CallbackQueryData(BotState.MANAGE_CAT, catDto.getId().toString())
                                )
                        )
                        .build());
            } catch (JsonProcessingException e) {
                var msg = "Unable to create my cat card of " + catDto.toString() + " " + e.getMessage();
                log.log(Level.WARNING, msg);
            }
        });
    }

    @SneakyThrows
    public static InlineKeyboardMarkup backButton() {
        var row1 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup photoUploadDesision() {
        var row1 = new InlineKeyboardRow();
        var row2 = new InlineKeyboardRow();

        row1.add(InlineKeyboardButton.builder()
                .text("Подтвердить")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.ADD_CAT_ASK_NAME, null)
                        )
                )
                .build());
        row1.add(InlineKeyboardButton.builder()
                .text("Повторить")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.ADD_CAT, null)
                        )
                )
                .build());


        row2.add(InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(
                        om.writeValueAsString(
                                new CallbackQueryData(BotState.MAIN_MENU, null)
                        )
                )
                .build());

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .build();
    }
}
