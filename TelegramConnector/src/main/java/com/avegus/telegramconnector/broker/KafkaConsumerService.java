package com.avegus.telegramconnector.broker;


import com.avegus.commons.model.CatWithUserId;
import com.avegus.commons.model.CatsWithUserId;
import com.avegus.commons.model.UserIdDto;

public interface KafkaConsumerService {
    void consumeRandomCat(CatWithUserId catWithUserId);
    void consumeUserCats(CatsWithUserId catsWithUserId);
    void consumeUserCat(CatWithUserId catWithUserId);
    void consumeOutOfRandomCats(UserIdDto userId);
}
