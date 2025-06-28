package com.avegus.telegramconnector.broker;

import com.avegus.commons.model.CatWithUserId;
import com.avegus.commons.model.CatsWithUserId;
import com.avegus.commons.model.UserIdDto;
import com.avegus.telegramconnector.bot.sender.MessageSender;
import com.avegus.telegramconnector.factory.InlineKeyboardFactory;
import com.avegus.telegramconnector.model.enums.Captions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.avegus.telegramconnector.factory.InlineKeyboardFactory.menuMarkup;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final MessageSender messageSender;

    @KafkaListener(topics = "${topics.cat.response-random}", groupId = "telegram-connector")
    @Override
    public void consumeRandomCat(CatWithUserId catWithUserId) {
        log.info("Consumed random cat {}", catWithUserId);

        var markup = InlineKeyboardFactory.catRatingMarkup(
                catWithUserId.getCat().getId().toString(),
                catWithUserId.getCat().getLikesCount(),
                catWithUserId.getCat().getDislikesCount()
        );
        messageSender.sendPhotoWithMarkup(
                catWithUserId.getUserId(),
                catWithUserId.getCat().generateDescription(),
                markup,
                catWithUserId.getCat().getFileId()
        );
    }

    @KafkaListener(topics = "${topics.cat.response-by-user}", groupId = "telegram-connector")
    @Override
    public void consumeUserCats(CatsWithUserId catsWithUserId) {
        log.info("Consumed user cats {}", catsWithUserId);

        messageSender.sendMarkup(
                catsWithUserId.getUserId(),
                InlineKeyboardFactory.myCatsMarkup(catsWithUserId.getCats()),
                Captions.MY_CATS
        );
    }

    @KafkaListener(topics = "${topics.cat.response-by-id}", groupId = "telegram-connector")
    @Override
    public void consumeUserCat(CatWithUserId catWithUserId) {
        log.info("Consumed user cat {}", catWithUserId);

        var description = catWithUserId.getCat().generateDescriptionWithLikes(
                catWithUserId.getCat().getLikesCount().toString(),
                catWithUserId.getCat().getDislikesCount().toString()
        );
        messageSender.sendPhotoWithMarkup(
                catWithUserId.getUserId(),
                description,
                InlineKeyboardFactory.myCatCardMarkup(catWithUserId.getCat().getId().toString()),
                catWithUserId.getCat().getFileId()
        );
    }

    @KafkaListener(topics = "${topics.cat.out-of-random-cats}", groupId = "telegram-connector")
    @Override
    public void consumeOutOfRandomCats(UserIdDto userId) {
        log.info("Consumed out of random cats {}", userId);

        messageSender.sendMarkup(userId.getId(),
                menuMarkup(),
                Captions.OUT_OF_RANDOM_CATS);
    }
}
